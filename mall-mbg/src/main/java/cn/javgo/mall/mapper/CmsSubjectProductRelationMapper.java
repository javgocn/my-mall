package cn.javgo.mall.mapper;

import cn.javgo.mall.model.CmsSubjectProductRelation;
import cn.javgo.mall.model.CmsSubjectProductRelationExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CmsSubjectProductRelationMapper {
    long countByExample(CmsSubjectProductRelationExample example);

    int deleteByExample(CmsSubjectProductRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsSubjectProductRelation row);

    int insertSelective(CmsSubjectProductRelation row);

    List<CmsSubjectProductRelation> selectByExample(CmsSubjectProductRelationExample example);

    CmsSubjectProductRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") CmsSubjectProductRelation row, @Param("example") CmsSubjectProductRelationExample example);

    int updateByExample(@Param("row") CmsSubjectProductRelation row, @Param("example") CmsSubjectProductRelationExample example);

    int updateByPrimaryKeySelective(CmsSubjectProductRelation row);

    int updateByPrimaryKey(CmsSubjectProductRelation row);
}