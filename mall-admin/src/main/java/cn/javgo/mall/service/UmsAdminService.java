package cn.javgo.mall.service;

import cn.javgo.mall.dto.UmsAdminParam;
import cn.javgo.mall.dto.UpdateAdminPasswordParam;
import cn.javgo.mall.model.UmsAdmin;
import cn.javgo.mall.model.UmsResource;
import cn.javgo.mall.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台用户管理 Service
 */
public interface UmsAdminService {

    /**
     * 根据用户名获取后台用户
     * @param username 用户名
     * @return 后台用户
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     * @param umsAdminParam 用户注册参数
     * @return 后台用户
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的 JWT 的 token
     */
    String login(String username, String password);

    /**
     * 刷新 token
     * @param oldToken 旧的 token
     * @return 新的 token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户 id 获取用户
     * @param id 用户 id
     * @return 用户
     */
    UmsAdmin getItem(Long id);

    /**
     * 分页查询用户，根据用户名或昵称
     * @param keyword 关键字（用户名或昵称）
     * @param pageSize 每页数量
     * @param pageNum 页码
     * @return 用户列表
     */
    List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     * @param id 用户 id
     * @param admin 用户信息
     * @return 修改结果
     */
    int update(Long id,UmsAdmin admin);

    /**
     * 删除指定用户
     * @param id 用户 id
     * @return 删除结果
     */
    int delete(Long id);

    /**
     * 修改用户角色关系
     * 说明：因为该操作涉及到两张表的修改，如果不加上 @Transactional 注解，当修改失败时，事务不会回滚，导致数据不一致。
     *      加上 @Transactional 注解后，当修改失败时，事务会回滚，数据一致。
     *
     * @param adminId 用户 id
     * @param roleIds 角色 id 列表
     * @return 修改结果
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户角色列表
     * @param adminId 用户 id
     * @return 角色列表
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 获取用户的资源列表
     * @param adminId 用户 id
     * @return 资源列表
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 修改密码
     * @param updatePasswordParam 修改密码参数
     * @return 修改结果
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    /**
     * 获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 获取缓存服务
     */
    UmsAdminCacheService getCacheService();
}
