package cn.javgo.mall.config;

import cn.javgo.mall.common.config.BaseSwaggerConfig;
import cn.javgo.mall.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API 文档相关配置
 */
@Configuration
@EnableSwagger2 // 开启 Swagger
public class SwaggerConfig extends BaseSwaggerConfig {
    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("cn.javgo.mall.controller") // 扫描的 Controller 包
                .title("mall 后台系统") // 文档标题
                .description("mall 后台相关接口文档") // 文档描述
                .contactName("javgo") // 联系人姓名
                .version("1.0") // 版本号
                .enableSecurity(true) // 是否需要配置 JWT 登录认证
                .build();
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }
}
