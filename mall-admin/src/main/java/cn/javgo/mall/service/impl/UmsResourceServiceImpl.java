package cn.javgo.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.javgo.mall.mapper.UmsResourceMapper;
import cn.javgo.mall.model.UmsResource;
import cn.javgo.mall.model.UmsResourceExample;
import cn.javgo.mall.service.UmsAdminCacheService;
import cn.javgo.mall.service.UmsResourceService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源管理 Service 实现类
 */
@Service
public class UmsResourceServiceImpl implements UmsResourceService {

    @Autowired
    private UmsResourceMapper resourceMapper;

    @Autowired
    private UmsAdminCacheService adminCacheService;

    @Override
    public int create(UmsResource umsResource) {
        umsResource.setCreateTime(new Date());
        return resourceMapper.insert(umsResource);
    }

    @Override
    public int update(Long id, UmsResource umsResource) {
        umsResource.setId(id);
        // 更新数据库资源
        int count = resourceMapper.updateByPrimaryKeySelective(umsResource);
        // 删除缓存中的资源
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public UmsResource getItem(Long id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        // 删除数据库资源
        int count = resourceMapper.deleteByPrimaryKey(id);
        // 删除缓存中的资源
        adminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        // 创建查询条件
        UmsResourceExample example = new UmsResourceExample();
        UmsResourceExample.Criteria criteria = example.createCriteria();
        // 添加分类 id 查询条件
        if (categoryId != null){
            criteria.andCategoryIdEqualTo(categoryId);
        }
        // 添加名称关键字查询条件
        if (StrUtil.isNotEmpty(nameKeyword)){
            criteria.andNameLike("%" + nameKeyword + "%");
        }
        // 添加 url 关键字查询条件
        if (StrUtil.isNotEmpty(urlKeyword)){
            criteria.andUrlLike("%" + urlKeyword + "%");
        }
        return resourceMapper.selectByExample(example);
    }

    @Override
    public List<UmsResource> listAll() {
        return resourceMapper.selectByExample(new UmsResourceExample());
    }
}
