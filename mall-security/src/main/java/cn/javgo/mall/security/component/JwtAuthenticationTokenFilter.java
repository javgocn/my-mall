package cn.javgo.mall.security.component;

import cn.javgo.mall.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 登录授权过滤器
 * 说明：
 * 1.为什么选择将自定义的 JWT 登录授权过滤器继承 OncePerRequestFilter？
 *   因为 OncePerRequestFilter 是一个过滤器基类，它可以确保我们的过滤器只被调用一次。
 * 2.为什么 JWT 登录授权过滤器只需要调用一次？
 *   因为 JWT 的特性是无状态，它不需要在服务端记录用户的状态，所以 JWT 登录授权过滤器只需要在用户登录成功后调用一次获取到 JWT 令牌，然后将
 *   JWT 令牌响应给客户端，客户端后续的请求都将在请求头中携带 JWT 令牌访问服务端资源，服务端只需要校验 JWT 的有效性即可。
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 重写 doFilterInternal 方法，从请求头中获取 JWT 令牌并校验，如果校验通过，就将用户信息封装到 Authentication 中，最后将
     * Authentication 设置到 Spring Security 的全局上下文中，供后续业务调用。如果 SecurityContextHolder 中已经存在 Authentication，
     * 说明当前请求已经登录过了，直接放行即可。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)){
            String authToken = authHeader.substring(this.tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            LOGGER.info("checking username:{}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                // 从数据库中加载用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken,userDetails)){
                    // 将用户信息和用户权限信息封装为 Authentication 对象
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    // 将请求信息封装到 Authentication 对象中，方便后续校验
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
