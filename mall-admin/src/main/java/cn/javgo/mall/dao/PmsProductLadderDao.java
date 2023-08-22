package cn.javgo.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import cn.javgo.mall.model.PmsProductLadder;

/**
 * 会员阶梯价格 Dao
 */
@Mapper
public interface PmsProductLadderDao {
    
    /**
     * 批量创建商品阶梯价格
     * 
     * @param productLadderList 商品阶梯价格列表
     * @return 创建的数量
     */
    int insertList(@Param("list") List<PmsProductLadder> productLadderList);
}
