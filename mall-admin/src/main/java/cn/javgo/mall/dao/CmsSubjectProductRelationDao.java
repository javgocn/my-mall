package cn.javgo.mall.dao;

import cn.javgo.mall.model.CmsSubjectProductRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专题商品关系自定义 Dao
 * 专题商品关系数据库表：cms_subject_product_relation
 * 作用：用于存储商品和专题的关系，所谓专题就是一些商品的集合，可以把一些商品放在一起展示。如：新品专区、热卖专区、推荐专区等
 * 字段：
 *   id：主键
 *   subjectId：专题 id
 *   productId：商品 id
 */
@Mapper
public interface CmsSubjectProductRelationDao {

    int insertList(@Param("list") List<CmsSubjectProductRelation> subjectProductRelationList);
}
