package cn.javgo.mall.service;

import cn.javgo.mall.model.UmsMenu;
import cn.javgo.mall.model.UmsResource;
import cn.javgo.mall.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台用户角色管理 Service
 */
public interface UmsRoleService {

    /**
     * 创建角色
     * @param umsRole 角色
     * @return 创建结果
     */
    int create(UmsRole umsRole);

    /**
     * 修改角色
     * @param id 角色 id
     * @param role 角色
     * @return 修改结果
     */
    int update(Long id, UmsRole role);

    /**
     * 批量删除角色
     * @param ids 角色 id 集合
     * @return 删除结果
     */
    int delete(List<Long> ids);

    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<UmsRole> listAll();

    /**
     * 分页获取角色
     * @param keyword 关键字(角色名称)
     * @param pageSize 每页数量
     * @param pageNum 页码
     * @return 角色列表
     */
    List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据管理员 id 获取对应菜单
     * @param adminId 管理员 id
     * @return 菜单列表
     */
    List<UmsMenu> getMenuList(Long adminId);

    /**
     * 根据角色 id 获取对应菜单
     * @param roleId 角色 id
     * @return 菜单列表
     */
    List<UmsMenu> listMenu(Long roleId);


    /**
     * 根据角色 id 获取对应资源
     * @param roleId 角色 id
     * @return 资源列表
     */
    List<UmsResource> listResource(Long roleId);

    /**
     * 给角色分配菜单
     * 说明：该操作同时涉及到角色与菜单的关联表，因此需要使用事务来保证数据一致性
     *
     * @param roleId 角色 id
     * @param menuIds 菜单 id 集合
     * @return 分配结果
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 给角色分配资源
     * 说明：该操作同时涉及到角色与资源的关联表，因此需要使用事务来保证数据一致性
     *
     * @param roleId 角色 id
     * @param resourceIds 资源 id 集合
     * @return 分配结果
     */
    int allocResource(Long roleId, List<Long> resourceIds);
}
