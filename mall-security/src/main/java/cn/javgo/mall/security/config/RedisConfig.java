package cn.javgo.mall.security.config;

import cn.javgo.mall.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 配置类
 */
@EnableCaching // 开启缓存
@Configuration
public class RedisConfig extends BaseRedisConfig {
}
