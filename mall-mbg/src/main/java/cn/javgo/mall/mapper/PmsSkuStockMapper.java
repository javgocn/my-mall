package cn.javgo.mall.mapper;

import cn.javgo.mall.model.PmsSkuStock;
import cn.javgo.mall.model.PmsSkuStockExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PmsSkuStockMapper {
    long countByExample(PmsSkuStockExample example);

    int deleteByExample(PmsSkuStockExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsSkuStock row);

    int insertSelective(PmsSkuStock row);

    List<PmsSkuStock> selectByExample(PmsSkuStockExample example);

    PmsSkuStock selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") PmsSkuStock row, @Param("example") PmsSkuStockExample example);

    int updateByExample(@Param("row") PmsSkuStock row, @Param("example") PmsSkuStockExample example);

    int updateByPrimaryKeySelective(PmsSkuStock row);

    int updateByPrimaryKey(PmsSkuStock row);
}