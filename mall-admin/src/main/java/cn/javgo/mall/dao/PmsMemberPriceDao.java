package cn.javgo.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.javgo.mall.model.PmsMemberPrice;

/**
 * 会员价格 Dao
 */
@Mapper
public interface PmsMemberPriceDao {
    
    /**
     * 批量创建
     * 
     * @param memberPriceList 会员价格列表
     * @return 创建的数量
     */
    int insertList(@Param("list") List<PmsMemberPrice> memberPriceList);
}
