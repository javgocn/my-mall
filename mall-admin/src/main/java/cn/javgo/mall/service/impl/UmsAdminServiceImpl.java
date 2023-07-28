package cn.javgo.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.javgo.mall.bo.AdminUserDetails;
import cn.javgo.mall.common.exception.Asserts;
import cn.javgo.mall.common.util.RequestUtil;
import cn.javgo.mall.dao.UmsAdminRoleRelationDao;
import cn.javgo.mall.dto.UmsAdminParam;
import cn.javgo.mall.dto.UpdateAdminPasswordParam;
import cn.javgo.mall.mapper.UmsAdminLoginLogMapper;
import cn.javgo.mall.mapper.UmsAdminMapper;
import cn.javgo.mall.mapper.UmsAdminRoleRelationMapper;
import cn.javgo.mall.model.*;
import cn.javgo.mall.security.util.JwtTokenUtil;
import cn.javgo.mall.security.util.SpringUtil;
import cn.javgo.mall.service.UmsAdminCacheService;
import cn.javgo.mall.service.UmsAdminService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 后台用户管理 Service 实现类
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UmsAdminMapper adminMapper;

    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;

    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;

    @Autowired
    private UmsAdminLoginLogMapper loginLogMapper;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        // 先从缓存中获取用户信息
        UmsAdmin admin = getCacheService().getAdmin(username);
        // 如果缓存中存在用户信息，则直接返回
        if (admin != null) return admin;
        // 如果缓存中不存在用户信息，则从数据库中获取用户信息
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0){
            admin = adminList.get(0);
            // 将用户信息添加到缓存中
            getCacheService().setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        // 构建 UmsAdmin 对象
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam,umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);

        // 查询数据库中是否有相同用户名的用户,已经存在则不再注册，直接返回 null
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = adminMapper.selectByExample(example);
        if (umsAdminList.size() > 0) return null;

        // 用户不存在，进行添加操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        // 密码需要客户端加密后传递
        try{
            // 根据用户名从数据库中获取用户信息
            UserDetails userDetails = loadUserByUsername(username);
            // 进行密码匹配
            if (!passwordEncoder.matches(password,userDetails.getPassword())){
                Asserts.fail("密码不正确");
            }
            // 检查用户是否被禁用
            if (!userDetails.isEnabled()){
                Asserts.fail("账号已被禁用");
            }
            // 封装用户信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            // 将用户信息存储到 Security 上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 生成 token
            token = jwtTokenUtil.generateToken(userDetails);
            // 添加登录记录
            insertLoginLog(username);
        }catch (AuthenticationException e){
            LOGGER.warn("登录异常:{}",e.getMessage());
        }
        return token;
    }

    /**
     * 添加用户登录记录
     * @param username 用户名
     */
    private void insertLoginLog(String username){
        // 获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin == null) return;

        // 构建 UmsAdminLoginLog 对象
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(RequestUtil.getRequestIp(request));

        // 添加登录记录
        loginLogMapper.insert(loginLog);
    }

    /**
     * 根据用户名修改登录时间(留作扩展)
     * @param username 用户名
     */
    private void updateLoginTimeByUsername(String username) {
        UmsAdmin record = new UmsAdmin();
        record.setLoginTime(new Date());
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        adminMapper.updateByExampleSelective(record, example);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public UmsAdmin getItem(Long id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        // 根据用户名或昵称模糊查询
        if (!StrUtil.isEmpty(keyword)){
            criteria.andUsernameLike("%" + keyword + "%");
            example.or(example.createCriteria().andNickNameLike("%" + keyword + "%"));
        }
        return adminMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = adminMapper.selectByPrimaryKey(id);
        // 如果密码没有改变，则不修改密码
        if (rawAdmin.getPassword().equals(admin.getPassword())){
            admin.setPassword(null);
        }else {
            // 密码为空，不修改密码
            if (StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else {
                // 密码加密后再修改
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        // 更新用户信息
        int count = adminMapper.updateByPrimaryKeySelective(admin);
        // 更新缓存
        getCacheService().delAdmin(id);
        return count;
    }

    @Override
    public int delete(Long id) {
        // 更新缓存
        getCacheService().delAdmin(id);
        // 删除用户
        int count = adminMapper.deleteByPrimaryKey(id);
        // 更新用户资源缓存
        getCacheService().delResourceList(id);
        return count;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        // 先删除原来的关系，即删除原来的角色
        UmsAdminRoleRelationExample adminRoleRelationExample = new UmsAdminRoleRelationExample();
        adminRoleRelationExample.createCriteria().andAdminIdEqualTo(adminId);
        adminRoleRelationMapper.deleteByExample(adminRoleRelationExample);
        // 建立新关系
        if (!CollectionUtils.isEmpty(roleIds)){
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationDao.insertList(list);
        }
        // 更新缓存
        getCacheService().delResourceList(adminId);
        return count;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return adminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        // 从缓存中获取资源列表
        List<UmsResource> resourceList = getCacheService().getResourceList(adminId);
        if (CollUtil.isNotEmpty(resourceList)){
            return resourceList;
        }
        // 从数据库中获取资源列表
        resourceList = adminRoleRelationDao.getResourceList(adminId);
        // 将资源列表添加到缓存中
        if (CollUtil.isNotEmpty(resourceList)){
            getCacheService().setResourceList(adminId,resourceList);
        }
        return resourceList;
    }

    /**
     * 修改密码
     * @param param 修改密码参数
     * @return -1:用户名为空，-2:用户不存在，-3:旧密码错误，1:修改成功
     */
    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        // 检查用户名、旧密码、新密码是否为空
        if (StrUtil.isEmpty(param.getUsername())
                || StrUtil.isEmpty(param.getOldPassword())
                || StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        // 构造查询条件，根据用户名查询用户信息
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(param.getUsername());
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (CollUtil.isEmpty(adminList)){
            return -2;
        }
        // 判断旧密码是否正确
        UmsAdmin umsAdmin = adminList.get(0);
        if (!passwordEncoder.matches(param.getOldPassword(),umsAdmin.getPassword())){
            return -3;
        }
        // 更新密码
        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        adminMapper.updateByPrimaryKey(umsAdmin);
        // 更新缓存
        getCacheService().delAdmin(umsAdmin.getId());
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null){
            List<UmsResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin,resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public UmsAdminCacheService getCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }
}
