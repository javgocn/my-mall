package cn.javgo.mall.security.component;

import cn.hutool.json.JSONUtil;
import cn.javgo.mall.common.api.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：没有权限访问时
 * 说明：
 * 1. AccessDeniedHandler 是一个接口，它有一个方法 handle，这个方法的作用是当用户访问某个受保护的资源，但是权限不足的时候，就会抛出
 *    AccessDeniedException 这个异常，这个异常会被 ExceptionTranslationFilter 这个过滤器捕获到，然后调用 AccessDeniedHandler
 *    的 handle 方法来处理异常。
 * 2. 与 RestAuthenticationEntryPoint 类似，由于是前后端分离架构，所以这里我们也不需要重定向到登录页面，只需要返回一个 JSON 格式的
 *    数据即可。
 */
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonResult.forbidden(accessDeniedException.getMessage())));
        response.getWriter().flush();
    }
}
