package cn.javgo.mall.security.component;

import cn.javgo.mall.security.config.IgnoreUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 动态权限过滤器，用于实现基于路径的动态权限过滤
 * 说明：
 * 1.AbstractSecurityInterceptor 是 Spring Security 中用于处理 URL 的授权决策过滤器，它的核心其实就是调用 beforeInvocation() 方法，
 *  该方法的作用是在方法执行前，通过调用 SecurityMetadataSource 的 getAttributes() 方法获取被拦截 URL 所需的全部权限，再调用
 *  AccessDecisionManager 的 decide() 方法进行鉴权操作。
 * 2.这里我们自定义了 AccessDecisionManager 的鉴权逻辑，因此需要通过调用父类的 setAccessDecisionManager() 方法将我们自定义的
 *  AccessDecisionManager 注入到 AbstractSecurityInterceptor 中。
 */
public class DynamicSecurityFilter extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    public void setMyAccessDecisionManager(DynamicAccessDecisionManager dynamicAccessDecisionManager) {
        super.setAccessDecisionManager(dynamicAccessDecisionManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        // OPTIONS 请求直接放行
        if (httpRequest.getMethod().equals(HttpMethod.OPTIONS.toString())){
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            return;
        }
        // 白名单请求直接放行
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String path : ignoreUrlsConfig.getUrls()) {
            if (antPathMatcher.match(path, httpRequest.getRequestURI())){
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
                return;
            }
        }
        // 调用父类的 beforeInvocation() 方法进行鉴权操作，在内部实现其实就是调用了 AccessDecisionManager 的 decide() 方法
        // 然而我们由于自定义了 AccessDecisionManager 的鉴权逻辑，因此最终会按照我们自定义的 decide() 方法进行鉴权操作
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try{
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.dynamicSecurityMetadataSource;
    }
}
