package cn.javgo.mall.mapper;

import cn.javgo.mall.model.PmsProductOperateLog;
import cn.javgo.mall.model.PmsProductOperateLogExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PmsProductOperateLogMapper {
    long countByExample(PmsProductOperateLogExample example);

    int deleteByExample(PmsProductOperateLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsProductOperateLog row);

    int insertSelective(PmsProductOperateLog row);

    List<PmsProductOperateLog> selectByExample(PmsProductOperateLogExample example);

    PmsProductOperateLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") PmsProductOperateLog row, @Param("example") PmsProductOperateLogExample example);

    int updateByExample(@Param("row") PmsProductOperateLog row, @Param("example") PmsProductOperateLogExample example);

    int updateByPrimaryKeySelective(PmsProductOperateLog row);

    int updateByPrimaryKey(PmsProductOperateLog row);
}