package cn.javgo.mall.service;

import java.util.List;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.javgo.mall.dto.PmsProductParam;
import cn.javgo.mall.dto.PmsProductQueryParam;
import cn.javgo.mall.dto.PmsProductResult;
import cn.javgo.mall.model.PmsProduct;

/**
 * 商品管理 Service
 */
public interface PmsProductService {
    
    /**
     * 创建商品
     * 说明：
     * 由于创建商品时，需要创建多个表的数据，因此使用了 @Transactional 注解，表示该方法是一个事务方法。同时，我们将事务的隔离级别设置为
     * Isolation.DEFAULT，表示使用数据库的默认隔离级别（MySQL 的默认隔离级别是 REPEATABLE_READ 可重复读）。事务的传播行为设置为
     * Propagation.REQUIRED，表示如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。
     * 
     * @param productParam 包含商品信息的参数
     * @return 创建商品的结果
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation =  Propagation.REQUIRED)
    int create(PmsProductParam productParam);

    /**
     * 根据商品编号获取更新信息
     * 
     * @param id 商品编号
     * @return 更新信息
     */
    PmsProductResult getUpdateInfo(Long id);

    /**
     * 更新商品
     * 
     * @param id 商品编号
     * @param productParam 包含商品信息的参数
     * @return 更新商品的结果
     */
    @Transactional
    int update(Long id, PmsProductParam productParam);

    /**
     * 分页查询商品
     * 
     * @param productQueryParam 包含商品查询参数的参数
     * @param pageSize 每页数量
     * @param pageNum 页码
     * @return
     */
    List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * 根据编号批量修改商品审核状态
     * 
     * @param ids 商品编号列表
     * @param verifyStatus 审核状态
     * @param detail 审核详情
     * @return 更新商品审核状态的结果
     */
    @Transactional
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 根据编号批量修改商品上架状态
     * 
     * @param ids 商品编号列表
     * @param publishStatus 上架状态
     * @return 更新商品上架状态的结果
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 根据编号批量修改商品推荐状态
     * 
     * @param ids 商品编号列表
     * @param recommendStatus 推荐状态
     * @return 更新商品推荐状态的结果
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 根据编号批量修改新品状态
     * 
     * @param ids 商品编号列表
     * @param newStatus 新品状态
     * @return 更新新品状态的结果
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量删除商品（并不是真的删除，而是将商品的删除状态修改为已删除）
     * 
     * @param ids 商品编号列表
     * @param deleteStatus 删除状态
     * @return 更新删除状态的结果
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 根据关键字（商品名称或者货号）模糊查询商品
     * 
     * @param keyword 关键字（商品名称或者货号）
     * @return 商品列表
     */
    List<PmsProduct> list(String keyword);
}
