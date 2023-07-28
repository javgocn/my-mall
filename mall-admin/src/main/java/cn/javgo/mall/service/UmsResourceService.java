package cn.javgo.mall.service;

import cn.javgo.mall.model.UmsResource;

import java.util.List;

/**
 * 后台资源管理 Service
 */
public interface UmsResourceService {

    /**
     * 添加资源
     * @param umsResource 资源
     * @return 添加结果
     */
    int create(UmsResource umsResource);

    /**
     * 修改资源
     * @param id 资源 id
     * @param umsResource 资源
     * @return 修改结果
     */
    int update(Long id, UmsResource umsResource);

    /**
     * 获取资源详情
     * @param id 资源 id
     * @return 资源
     */
    UmsResource getItem(Long id);

    /**
     * 删除资源
     * @param id 资源 id
     * @return 删除结果
     */
    int delete(Long id);

    /**
     * 分页查询资源
     * @param categoryId 资源分类 id
     * @param nameKeyword 资源名称关键字
     * @param urlKeyword 资源 url 关键字
     * @param pageSize 每页数量
     * @param pageNum 页码
     * @return 资源列表
     */
    List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

    /**
     * 查询全部资源
     * @return 资源列表
     */
    List<UmsResource> listAll();
}
