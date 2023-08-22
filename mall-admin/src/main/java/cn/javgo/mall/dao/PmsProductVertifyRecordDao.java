package cn.javgo.mall.dao;

import cn.javgo.mall.model.PmsProductVertifyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品审核记录自定义 Dao
 * 说明：商品审核记录表（pms_product_vertify_record）主要用来记录商品审核操作的记录，包括审核人员、审核时间、审核备注等信息。
 *      该表字段如下：
 *      id：主键
 *      productId：商品id
 *      createTime：创建时间
 *      vertify_man：审核人员
 *      status：审核状态：0->未审核；1->审核通过
 *      detail：反馈详情，简单来说就是审核备注
 */
@Mapper
public interface PmsProductVertifyRecordDao {

    /**
     * 批量创建
     * @param list 商品审核记录列表
     * @return int 影响行数
     */
    int insertList(@Param("list") List<PmsProductVertifyRecord> list);
}
