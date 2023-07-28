package cn.javgo.mall.service;

import cn.javgo.mall.model.UmsAdmin;
import cn.javgo.mall.model.UmsResource;

import java.util.List;

/**
 * 后台用户缓存操作 Service
 */
public interface UmsAdminCacheService {

    /**
     * 删除后台用户缓存
     * @param adminId 后台用户 id
     */
    void delAdmin(Long adminId);

    /**
     * 删除后台用户资源列表缓存
     * @param adminId 后台用户 id
     */
    void delResourceList(Long adminId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     * @param roleId 角色 id
     */
    void delResourceListByRole(Long roleId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     * @param roleIds 角色 id 列表
     */
    void delResourceListByRoleIds(List<Long> roleIds);

    /**
     * 当资源信息改变时，删除资源项目后台用户缓存
     * @param resourceId 资源 id
     */
    void delResourceListByResource(Long resourceId);

    /**
     * 获取缓存后台用户信息
     * @param username 用户名
     * @return 后台用户
     */
    UmsAdmin getAdmin(String username);

    /**
     * 设置缓存后台用户信息
     * @param admin 后台用户
     */
    void setAdmin(UmsAdmin admin);

    /**
     * 获取缓存后台用户资源列表
     * @param adminId 后台用户 id
     * @return 后台用户资源列表
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 设置缓存后台用户资源列表
     * @param adminId 后台用户 id
     * @param resourceList 后台用户资源列表
     */
    void setResourceList(Long adminId, List<UmsResource> resourceList);
}
