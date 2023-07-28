package cn.javgo.mall.security.config;

import cn.javgo.mall.security.component.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置类，这里仅用于配置 SecurityFilterChain，即 Spring Security 的过滤器链。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired(required = false)
    private DynamicSecurityService dynamicSecurityService;

    @Autowired(required = false)
    private DynamicSecurityFilter dynamicSecurityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 开启 HttpSecurity 授权配置
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();
        // 不需要保护的资源路径允许访问
        for (String url : ignoreUrlsConfig.getUrls()) {
            registry.antMatchers(url).permitAll();
        }
        // 允许跨域请求的 OPTIONS 请求
        registry.antMatchers(HttpMethod.OPTIONS).permitAll();
        // 其他任何请求都需要身份认证
        registry.and().authorizeRequests()
                .anyRequest().authenticated()
                .and()
                // 关闭跨站请求防护
                .csrf().disable()
                // 不使用 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 自定义权限拒绝处理类
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                // 自定义权限拦截器 JWT 过滤器, 在 UsernamePasswordAuthenticationFilter 之前执行
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 如果有动态权限配置，则增加动态权限校验过滤器
        if (dynamicSecurityService != null){
            registry.and().addFilterBefore(dynamicSecurityFilter, FilterSecurityInterceptor.class);
        }
        return httpSecurity.build();
    }
}
