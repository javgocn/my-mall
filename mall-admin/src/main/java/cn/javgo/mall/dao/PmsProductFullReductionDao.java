package cn.javgo.mall.dao;

import cn.javgo.mall.model.PmsProductFullReduction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 商品满减 Dao
 */
@Mapper
public interface PmsProductFullReductionDao {

    /**
     * 批量创建
     * @param productFullReductionList 商品满减列表
     * @return int
     */
    int insertList(@Param("list") List<PmsProductFullReduction> productFullReductionList);
}
