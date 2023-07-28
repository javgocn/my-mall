package cn.javgo.mall.service;

import cn.javgo.mall.model.UmsResourceCategory;

import java.util.List;

/**
 * 后台资源分类管理 Service
 */
public interface UmsResourceCategoryService {

    /**
     * 获取所有资源分类
     * @return 资源分类列表
     */
    List<UmsResourceCategory> listAll();

    /**
     * 创建资源分类
     * @param umsResourceCategory 资源分类
     * @return 创建结果
     */
    int create(UmsResourceCategory umsResourceCategory);

    /**
     * 修改资源分类
     * @param id 资源分类 id
     * @param umsResourceCategory 资源分类
     * @return 修改结果
     */
    int update(Long id, UmsResourceCategory umsResourceCategory);

    /**
     * 删除资源分类
     * @param id 资源分类 id
     * @return 删除结果
     */
    int delete(Long id);
}
