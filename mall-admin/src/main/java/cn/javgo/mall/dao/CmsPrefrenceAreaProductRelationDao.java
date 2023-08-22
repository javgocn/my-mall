package cn.javgo.mall.dao;

import cn.javgo.mall.model.CmsPrefrenceAreaProductRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义商品优选和商品关系操作
 * 商品优选和商品关系数据库表：cms_prefrence_area_product_relation
 * 作用：用于存储商品优选和商品的关系，即商品属于哪个商品优选。例如：手机属于数码优选。
 * 字段：
 *  id：主键
 *  prefrence_area_id：商品优选的主键
 *  product_id：商品的主键
 */
@Mapper
public interface CmsPrefrenceAreaProductRelationDao {

    /**
     * 批量创建商品优选和商品的关系
     * @param prefrenceAreaProductRelationList 商品优选和商品的关系列表
     * @return 创建的数量
     */
    int insertList(@Param("list") List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList);
}
