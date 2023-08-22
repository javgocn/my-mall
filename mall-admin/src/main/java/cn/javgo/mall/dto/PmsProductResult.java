package cn.javgo.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询单个商品进行修改时返回的结果
 * 说明：
 *   1.为什么要创建该类？
 *      因为在查询单个商品进行修改时，需要返回的结果与创建商品时需要返回的结果不一样，因此需要创建该类。在该类中我们在 PmsProductParam 的
 *      基础上增加了商品所选分类的父id 属性，即 cateParentId 属性。cateParentId 的主要目的是为了在前端页面中显示商品所选分类的父分类。
 *   2.为什么不直接在 PmsProductParam 类中添加商品所选分类的父id 属性？
 *      因为商品所选分类的父id 属性只有在查询单个商品进行修改时才需要返回，而在创建商品时不需要返回，因此将商品所选分类的父id 属性放到
 *      PmsProductResult 类中。
 */
public class PmsProductResult extends PmsProductParam{
    
    @Setter
    @Getter
    @ApiModelProperty("商品所选分类的父id")
    private Long cateParentId;
}
