package cn.javgo.mall.dao;

import cn.javgo.mall.model.UmsMenu;
import cn.javgo.mall.model.UmsResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后台用户角色管理 Dao
 */
@Mapper
public interface UmsRoleDao {

    /**
     * 根据后台用户 id 获取对应菜单
     * @param adminId 管理员 id
     * @return 菜单列表
     */
    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);

    /**
     * 根据角色 id 获取对应菜单
     * @param roleId 角色 id
     * @return 菜单列表
     */
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色 id 获取对应资源
     * @param roleId 角色 id
     * @return 资源列表
     */
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);
}
