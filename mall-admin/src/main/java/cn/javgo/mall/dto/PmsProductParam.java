package cn.javgo.mall.dto;

import java.util.List;

import cn.javgo.mall.model.CmsPrefrenceAreaProductRelation;
import cn.javgo.mall.model.CmsSubjectProductRelation;
import cn.javgo.mall.model.PmsMemberPrice;
import cn.javgo.mall.model.PmsProduct;
import cn.javgo.mall.model.PmsProductAttributeValue;
import cn.javgo.mall.model.PmsProductFullReduction;
import cn.javgo.mall.model.PmsProductLadder;
import cn.javgo.mall.model.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 创建和修改商品时使用的请求参数
 * 说明：
 * 1. 该类继承了 PmsProduct，因此它拥有了 PmsProduct 的所有属性，但是它并不是 PmsProduct 的子类，因为我们使用了 @EqualsAndHashCode(callSuper = false) 注解，
 *    表示不调用父类的属性，这样做的目的是为了避免在创建和修改商品时，调用父类的属性，因为父类的属性是数据库中的字段，而在创建和修改商品时，不需要调用数据库中的字段。
 * 2. 该类的作用是在创建和修改商品时，接收前端传来的参数，然后将这些参数封装到该类中，最后将该类作为参数传递给 PmsProductService 类中的 create() 和 update() 方法。
 * 3. 该类的属性与前端传来的参数一一对应，基本上就是定义了一个一对多的情况，即一个商品可能对应多个阶梯价格、满减价格、会员价格、sku 库存、商品参数、商品属性、专题和优选等。
 */
@Data
@EqualsAndHashCode(callSuper = false) // callSuper = false 表示不调用父类的属性
public class PmsProductParam extends PmsProduct {
    
    @ApiModelProperty("商品阶梯价格设置")
    private List<PmsProductLadder> productLadderList;

    @ApiModelProperty("商品满减价格设置")
    private List<PmsProductFullReduction> productFullReductionList;

    @ApiModelProperty("商品会员价格设置")
    private List<PmsMemberPrice> memberPriceList;

    @ApiModelProperty("商品的 sku 库存信息")
    private List<PmsSkuStock> skuStockList;

    @ApiModelProperty("商品参数及自定义规格属性值")
    private List<PmsProductAttributeValue> productAttributeValueList;

    @ApiModelProperty("专题和商品关系")
    private List<CmsSubjectProductRelation> subjectProductRelationList;

    @ApiModelProperty("优选专区和商品的关系")
    private List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList;
}
