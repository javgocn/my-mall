package cn.javgo.mall.security.component;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.URLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 动态权限数据源，用于获取动态权限规则
 * 说明：
 * 1.FilterInvocationSecurityMetadataSource 是 SecurityMetadataSource 的子接口，用于获取与 URL 对应的权限或角色。
 * 2.SecurityMetadataSource 接口有三个方法：
 *   2.1.getAttributes() 方法：根据 URL，获取该 URL 所需要的权限或角色。
 *   2.2.getAllConfigAttributes() 方法：获取所有的权限或角色。
 *   2.3.supports() 方法：指示该类是否能够为指定的方法调用或 Web 请求提供 ConfigAttributes。
 */
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static Map<String,ConfigAttribute> configAttributeMap = null;

    @Autowired
    private DynamicSecurityService dynamicSecurityService;

    /**
     * 在创建对象后，@PostConstruct 注解的方法会被自动调用以加载资源 ANT 通配符和资源对应 MAP
     */
    @PostConstruct
    public void loadDataSource() {
        configAttributeMap = dynamicSecurityService.loadDataSource();
    }

    /**
     * 由于我们的后台资源规则都被缓存在了该类的 configAttributeMap 集合中，所以当每次后台资源发生变化时，我们都需要在对应的控制层
     * 清空该集合的缓存数据，以便重新加载最新的后台资源规则。
     * 1.清空 configAttributeMap 集合
     * 2.将 configAttributeMap 置为 null，使得 JVM 能够回收该对象
     */
    public void clearDataSource() {
        configAttributeMap.clear();
        configAttributeMap = null;
    }

    /**
     * 该方法用于根据 URL，获取该 URL 所需要的权限或角色。
     * @param object 受保护的资源对象，其实就是一个 FilterInvocation 对象，我们可以从中提取出当前请求的 URL。
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (configAttributeMap == null)
            this.loadDataSource();
        // 其中存储的其实就是一个 String 字符串代表当前 URL 的权限，如 1:商品管理
        List<ConfigAttribute> configAttributes = new ArrayList<>();
        String url = ((FilterInvocation) object).getRequestUrl();
        String path = URLUtil.getPath(url);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Iterator<String> iterator = configAttributeMap.keySet().iterator();
        while (iterator.hasNext()){
            String pattern = iterator.next();
            if(antPathMatcher.match(pattern,path)){
                configAttributes.add(configAttributeMap.get(pattern));
            }
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
