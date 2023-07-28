package cn.javgo.mall.service.impl;

import cn.javgo.mall.mapper.UmsResourceCategoryMapper;
import cn.javgo.mall.model.UmsResourceCategory;
import cn.javgo.mall.model.UmsResourceCategoryExample;
import cn.javgo.mall.service.UmsResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源分类管理 Service 实现类
 */
@Service
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {

    @Autowired
    private UmsResourceCategoryMapper umsResourceCategoryMapper;

    @Override
    public List<UmsResourceCategory> listAll() {
        UmsResourceCategoryExample example = new UmsResourceCategoryExample();
        // 设置排序规则为 sort 字段降序
        example.setOrderByClause("sort desc");
        return umsResourceCategoryMapper.selectByExample(example);
    }

    @Override
    public int create(UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        return umsResourceCategoryMapper.insert(umsResourceCategory);
    }

    @Override
    public int update(Long id, UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setId(id);
        return umsResourceCategoryMapper.updateByPrimaryKeySelective(umsResourceCategory);
    }

    @Override
    public int delete(Long id) {
        return umsResourceCategoryMapper.deleteByPrimaryKey(id);
    }
}
