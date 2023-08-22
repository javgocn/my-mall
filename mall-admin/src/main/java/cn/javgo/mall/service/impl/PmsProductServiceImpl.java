package cn.javgo.mall.service.impl;

import java.util.List;

import cn.javgo.mall.dao.*;
import cn.javgo.mall.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.javgo.mall.dto.PmsProductParam;
import cn.javgo.mall.dto.PmsProductQueryParam;
import cn.javgo.mall.dto.PmsProductResult;
import cn.javgo.mall.model.PmsMemberPrice;
import cn.javgo.mall.model.PmsProduct;
import cn.javgo.mall.service.PmsProductService;
import org.springframework.stereotype.Service;

/**
 * 商品管理 Service 实现类
 */
@Service
public class PmsProductServiceImpl implements PmsProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);

    // 商品管理 Dao（增加获取商品编辑信息方法）
    @Autowired
    private PmsProductDao productDao;

    // 商品管理 Mapper
    @Autowired
    private PmsProductMapper productMapper;

    // 会员价格管理 Dao（增加批量插入方法）
    @Autowired
    private PmsMemberPriceDao memberPriceDao;

    // 会员价格管理 Mapper
    @Autowired
    private PmsMemberPriceMapper memberPriceMapper;

    // 阶梯价格管理 Dao（增加批量插入方法）
    @Autowired
    private PmsProductLadderDao productLadderDao;

    // 阶梯价格管理 Mapper
    @Autowired
    private PmsProductLadderMapper productLadderMapper;

    // 满减价格管理 Dao （增加批量插入方法）
    @Autowired
    private PmsProductFullReductionDao productFullReductionDao;

    // 满减价格管理 Mapper
    @Autowired
    private PmsProductFullReductionMapper productFullReductionMapper;

    // 商品 sku 库存管理 Dao（增加批量插入和批量更新方法）
    @Autowired
    private PmsSkuStockDao skuStockDao;

    // 商品 sku 库存管理 Mapper
    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    // 商品参数及自定义规格属性管理 Dao（增加批量插入方法）
    @Autowired
    private PmsProductAttributeValueDao productAttributeValueDao;

    // 商品参数及自定义规格属性管理 Mapper
    @Autowired
    private PmsProductAttributeValueMapper productAttributeValueMapper;

    // 专题商品关系管理 Dao（增加批量插入方法）
    @Autowired
    private CmsSubjectProductRelationDao subjectProductRelationDao;

    // 专题商品关系管理 Mapper
    @Autowired
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;

    // 优选专区和产品关系管理 Dao（增加批量插入方法）
    @Autowired
    private CmsPrefrenceAreaProductRelationDao prefrenceAreaProductRelationDao;

    // 优选专区和产品关系管理 Mapper
    @Autowired
    private CmsPrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;

    // 商品审核记录管理 Dao（增加批量插入方法）
    @Autowired
    private PmsProductVertifyRecordDao productVertifyRecordDao;

    /**
     * 创建商品
     * @param productParam 包含商品信息的参数
     * @return 创建完成的数量
     */
    @Override
    public int create(PmsProductParam productParam) {
        int count;
        // 创建商品（多余的字段不会插入）
        PmsProduct product = productParam;
        // 设置 id 为 null,防止出现主键冲突
        product.setId(null);
        /*
            插入商品表
            说明：MBG 自动生成的 ProductMapper 有两个插入方法，insert 和 insertSelective
                - insert 方法是将传入的实体类的所有字段都插入数据库，如果实体类的某个字段没有设置值，那么数据库中对应的字段就是 null
                - insertSelective 方法只将传入的实体类中不为 null 的字段插入数据库，如果实体类的某个字段没有设置值，那么数据库中对应
                  的字段就是数据库设置的默认值
            由于实体类中的字段都是使用包装类型，所以不会出现字段为 null 的情况，这也就意味着 insert 和 insertSelective 方法的效果是一样的
            但是为了保持代码的一致性，这里使用 insertSelective 方法而已。
         */
        productMapper.insertSelective(product);
        /*
            获取刚刚插入的商品的 id
            说明：由于在插入商品时，使用了 insertSelective 方法，所以数据库中的 id 字段会自动生成，这里获取到的 id 就是数据库中
                 自动生成的 id。如果使用 insert 方法，那么这里获取到的 id 就是传入的实体类中的 id，而不是数据库中自动生成的 id。
                 这也是为什么在插入商品时，使用 insertSelective 方法的原因。
         */
        Long productId = product.getId();
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUpdateInfo'");
    }

    @Override
    public int update(Long id, PmsProductParam productParam) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateVerifyStatus'");
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePublishStatus'");
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateRecommendStatus'");
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNewStatus'");
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDeleteStatus'");
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }
    
}
