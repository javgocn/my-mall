package cn.javgo.mall.service;

import cn.javgo.mall.dto.UmsMenuNode;
import cn.javgo.mall.model.UmsMenu;

import java.util.List;

/**
 * 后台菜单管理 Service
 */
public interface UmsMenuService {

    /**
     * 创建后台菜单
     * @param umsMenu 后台菜单
     * @return 创建结果
     */
    int create(UmsMenu umsMenu);

    /**
     * 修改后台菜单
     * @param id 菜单 id
     * @param umsMenu 后台菜单
     * @return 修改结果
     */
    int update(Long id, UmsMenu umsMenu);

    /**
     * 根据ID获取菜单详情
     * @param id 菜单 id
     * @return 菜单
     */
    UmsMenu getItem(Long id);

    /**
     * 删除后台菜单
     * @param id 菜单 id
     * @return 删除结果
     */
    int delete(Long id);

    /**
     * 分页查询后台菜单
     * @param parentId 父菜单 id
     * @param pageSize 每页数量
     * @param pageNum 页码
     * @return 菜单列表
     */
    List<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     * @return 菜单列表
     */
    List<UmsMenuNode> treeList();

    /**
     * 修改菜单显示状态
     * @param id 菜单 id
     * @param hidden 是否显示（0-显示，1-隐藏）
     * @return 修改结果
     */
    int updateHidden(Long id, Integer hidden);
}
