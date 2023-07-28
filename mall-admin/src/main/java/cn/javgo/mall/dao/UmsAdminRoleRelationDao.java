package cn.javgo.mall.dao;

import cn.javgo.mall.model.UmsAdminRoleRelation;
import cn.javgo.mall.model.UmsResource;
import cn.javgo.mall.model.UmsRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后台用户与角色管理自定义 Dao
 * 说明：下面我们在方法参数重使用了 MyBatis 提供的 @Param 注解，这个注解的作用是给参数取一个别名，如果只有一个参数并且在 <if> 里使用，则可
 * 以不加，但是为了代码的可读性，建议都加上。
 */
@Mapper
public interface UmsAdminRoleRelationDao {

    /**
     * 批量插入用户角色关系
     * @param adminRoleRelationList 用户角色关系列表
     * @return 插入数量
     */
    int insertList(@Param("list") List<UmsAdminRoleRelation> adminRoleRelationList);

    /**
     * 获取用户所有角色
     * @param adminId 用户 id
     * @return 角色列表
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    /**
     * 获取用户所有可访问资源
     * @param adminId 用户 id
     * @return 资源列表
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 获取资源相关用户 id 列表
     * @param resourceId 资源 id
     * @return 用户 id 列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}
