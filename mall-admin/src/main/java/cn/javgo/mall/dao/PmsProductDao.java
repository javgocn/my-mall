package cn.javgo.mall.dao;

import cn.javgo.mall.dto.PmsProductResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品管理自定义 Dao
 */
@Mapper
public interface PmsProductDao {

    /**
     * 获取商品编辑信息。所谓编辑信息，就是商品信息加上商品所选分类的父 id
     * @param id 商品id
     * @return 商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);
}
