package cn.javgo.mall.dao;

import cn.javgo.mall.model.PmsSkuStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品 SKU 管理自定义 Dao
 * 说明：
 * 1. SKU 是 Stock Keeping Unit 的简称，即库存量单位，可以是以件、盒、托盘等为单位。
 * 2. SPU 是 Standard Product Unit 的简称，即标准产品单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。
 * 举个例子：例如现在有一款 iPhone 11，他有不同的属性，如颜色（黑色，白色）、内存（64G，128G，256G）等，那么此时：
 *          - SKU 就是：iPhone 11 黑色 64G，iPhone 11 黑色 128G 等（即库存量单位）；
 *          - SPU 就是：iPhone 11（即标准产品单位）。
 *  用最通俗的话来说，SPU 是商品的公共属性集合，而 SKU 是商品的个性属性集合。
 */
@Mapper
public interface PmsSkuStockDao {

    /**
     * 批量插入操作
     * 说明：如果数据库中已经存在相同的记录，则不会执行插入操作。
     *
     * @param skuStockList SKU 列表
     * @return 插入操作影响的行数
     */
    int insertList(@Param("list") List<PmsSkuStock> skuStockList);

    /**
     * 批量插入或替换操作
     * 说明：如果数据库中已经存在相同的记录，则使用新记录替换掉原有记录。
     *
     * @param skuStockList SKU 列表
     * @return 插入或替换操作影响的行数
     */
    int replaceList(@Param("list") List<PmsSkuStock> skuStockList);

}
