package cn.javgo.mall.security.config;

import cn.javgo.mall.security.component.*;
import cn.javgo.mall.security.util.JwtTokenUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 配置类，将所有要用到的组件统一配置到这里，以免出现循环依赖等问题。
 */
@Configuration
public class CommonSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig(){
        return  new IgnoreUrlsConfig();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler(){
        return new RestfulAccessDeniedHandler();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicAccessDecisionManager dynamicAccessDecisionManager(){
        return new DynamicAccessDecisionManager();
    }

    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource(){
        return new DynamicSecurityMetadataSource();
    }

    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter(){
        return new DynamicSecurityFilter();
    }
}
