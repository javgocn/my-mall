package cn.javgo.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis 相关配置
 */
@Configuration
@EnableTransactionManagement // 开启事务管理
@MapperScan({"cn.javgo.mall.mapper","cn.javgo.mall.dao"}) // 扫描 Mapper 接口
public class MyBatisConfig {
}
