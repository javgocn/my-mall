package cn.javgo.mall.mapper;

import cn.javgo.mall.model.CmsTopic;
import cn.javgo.mall.model.CmsTopicExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CmsTopicMapper {
    long countByExample(CmsTopicExample example);

    int deleteByExample(CmsTopicExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsTopic row);

    int insertSelective(CmsTopic row);

    List<CmsTopic> selectByExampleWithBLOBs(CmsTopicExample example);

    List<CmsTopic> selectByExample(CmsTopicExample example);

    CmsTopic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") CmsTopic row, @Param("example") CmsTopicExample example);

    int updateByExampleWithBLOBs(@Param("row") CmsTopic row, @Param("example") CmsTopicExample example);

    int updateByExample(@Param("row") CmsTopic row, @Param("example") CmsTopicExample example);

    int updateByPrimaryKeySelective(CmsTopic row);

    int updateByPrimaryKeyWithBLOBs(CmsTopic row);

    int updateByPrimaryKey(CmsTopic row);
}