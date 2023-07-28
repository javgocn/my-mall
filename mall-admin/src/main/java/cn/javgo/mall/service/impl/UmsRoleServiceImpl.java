package cn.javgo.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.javgo.mall.dao.UmsRoleDao;
import cn.javgo.mall.mapper.UmsRoleMapper;
import cn.javgo.mall.mapper.UmsRoleMenuRelationMapper;
import cn.javgo.mall.mapper.UmsRoleResourceRelationMapper;
import cn.javgo.mall.model.*;
import cn.javgo.mall.service.UmsAdminCacheService;
import cn.javgo.mall.service.UmsRoleService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台用户角色管理 Service 实现类
 */
@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    @Autowired
    private UmsRoleMapper umsRoleMapper;

    @Autowired
    private UmsRoleMenuRelationMapper umsRoleMenuRelationMapper;

    @Autowired
    private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;

    @Autowired
    private UmsRoleDao roleDao;

    @Autowired
    private UmsAdminCacheService adminCacheService;

    @Override
    public int create(UmsRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        return umsRoleMapper.insert(role);
    }

    @Override
    public int update(Long id, UmsRole role) {
        role.setId(id);
        return umsRoleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int delete(List<Long> ids) {
        UmsRoleExample example = new UmsRoleExample();
        example.createCriteria().andIdIn(ids);
        // 删除数据库中的角色
        int count = umsRoleMapper.deleteByExample(example);
        // 删除缓存中的角色
        adminCacheService.delResourceListByRoleIds(ids);
        return count;
    }

    @Override
    public List<UmsRole> listAll() {
        return umsRoleMapper.selectByExample(new UmsRoleExample());
    }

    @Override
    public List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsRoleExample example = new UmsRoleExample();
        if (!StrUtil.isEmpty(keyword)){
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return umsRoleMapper.selectByExample(example);
    }

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return roleDao.getMenuList(adminId);
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        return roleDao.getMenuListByRoleId(roleId);
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        return roleDao.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        // 删除数据库中的原有关系
        UmsRoleMenuRelationExample example = new UmsRoleMenuRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleMenuRelationMapper.deleteByExample(example);
        // 添加新的关系
        for (Long menuId : menuIds) {
            UmsRoleMenuRelation relation = new UmsRoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            // 保存到数据库
            umsRoleMenuRelationMapper.insert(relation);
        }
        return menuIds.size();
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        // 删除数据库中的原有关系
        UmsRoleResourceRelationExample example = new UmsRoleResourceRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleResourceRelationMapper.deleteByExample(example);
        // 添加新的关系
        for (Long resourceId : resourceIds) {
            UmsRoleResourceRelation relation = new UmsRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            // 保存到数据库
            umsRoleResourceRelationMapper.insert(relation);
        }
        // 更新缓存
        adminCacheService.delResourceListByRole(roleId);
        return resourceIds.size();
    }
}
