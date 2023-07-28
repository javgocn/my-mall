package cn.javgo.mall.security.component;

import cn.hutool.json.JSONUtil;
import cn.javgo.mall.common.api.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：没有登录或 token 过期时
 * 说明：
 * 1. AuthenticationEntryPoint 由 ExceptionTranslationFilter 来调用，用来解决匿名用户访问无权限资源时的异常。所谓匿名用户，
 *    指的是没有登录的用户。在默认的情况下 Spring Security 的配置中，匿名用户是允许访问无权限资源的，但是如果匿名用户访问了需要权限
 *    的资源，就会抛出认证失败的 AuthenticationException 异常，这个异常会被 Spring Security 默认注册的 ExceptionTranslationFilter
 *    捕获到，然后调用 AuthenticationEntryPoint 的 commence() 方法来处理异常。
 * 2. 在默认情况下，Spring Security 的 ExceptionTranslationFilter 拦截到 AuthenticationException 异常后，会调用 AuthenticationEntryPoint
 *    将请求重定向到登录页面，因为我们是前后端分离的，所以这里我们不需要重定向，只需要返回一个 JSON 格式的数据即可。
 * 3. 由于我们的需求是自定义用户未登录或者 token 已过期时的返回结果，所以我们只需要在自定义的 AuthenticationEntryPoint 实现类中重写
 *   commence() 方法实现自定义返回结果逻辑即可。
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 方法说明：
     * 1.设置响应头为允许跨域，是为了解决前后端分离架构中的跨域问题。
     * 2.设置响应头为不缓存，是为了解决因为浏览器缓存导致的跨域问题。这是因为浏览器在跨域请求时，会先发送一个 OPTIONS 请求，这个请求是不带
     *   token 的，如果浏览器缓存了这个 OPTIONS 请求，那么就会导致后续的 POST、PUT、DELETE 等请求也不会携带 token，从而导致请求失败。
     *   所以，为了避免这个问题，我们需要设置响应头为不缓存。
     * 3.设置响应头为 UTF-8 编码，是为了解决中文乱码问题。
     * 4.设置响应头为 JSON 格式，是为了告诉浏览器返回的是 JSON 格式的数据，以便放回我们的通用返回结果。
     * 5.这里如果不手动刷新 response 的缓存，就会导致返回的数据无法及时刷新，而是等到缓存时间到了才会刷新，这样就会导致返回的数据不是我们
     *   预期的数据。
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonResult.unauthorized(authException.getMessage())));
        response.getWriter().flush();
    }
}
