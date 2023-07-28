package cn.javgo.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.javgo.mall.common.service.RedisService;
import cn.javgo.mall.dao.UmsAdminRoleRelationDao;
import cn.javgo.mall.mapper.UmsAdminRoleRelationMapper;
import cn.javgo.mall.model.UmsAdmin;
import cn.javgo.mall.model.UmsAdminRoleRelation;
import cn.javgo.mall.model.UmsAdminRoleRelationExample;
import cn.javgo.mall.model.UmsResource;
import cn.javgo.mall.service.UmsAdminCacheService;
import cn.javgo.mall.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台用户缓存操作 Service 实现类
 */
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;

    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;

    @Value("${redis.database}")
    private String REDIS_DATABASE;

    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;

    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Override
    public void delAdmin(Long adminId) {
        // 从数据库中获取 admin 对象
        UmsAdmin admin = adminService.getItem(adminId);
        if (admin != null){
            // 拼接 key
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public void delResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        // 从数据库中获取 adminId 列表
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<UmsAdminRoleRelation> adminIdList = adminRoleRelationMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(adminIdList)){
            // 拼接 key 的前缀（后面还需要拼接 adminId）
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            // 遍历 adminIdList 列表，获取到所有的 key
            List<String> keys = adminIdList.stream().map(adminId -> keyPrefix + adminId.getAdminId()).collect(Collectors.toList());
            // 删除所有的 key
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andRoleIdIn(roleIds);
        List<UmsAdminRoleRelation> relationList = adminRoleRelationMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(relationList)){
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relationList.stream().map(adminId -> keyPrefix + adminId.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIdList = adminRoleRelationDao.getAdminIdList(resourceId);
        if (CollUtil.isNotEmpty(adminIdList)){
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = adminIdList.stream().map(adminId -> keyPrefix + adminId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public UmsAdmin getAdmin(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (UmsAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(UmsAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
        redisService.set(key, admin, REDIS_EXPIRE);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        return (List<UmsResource>) redisService.get(key);
    }

    @Override
    public void setResourceList(Long adminId, List<UmsResource> resourceList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.set(key, resourceList, REDIS_EXPIRE);
    }
}
