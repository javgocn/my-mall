package cn.javgo.mall.config;

import cn.javgo.mall.model.UmsResource;
import cn.javgo.mall.security.component.DynamicSecurityService;
import cn.javgo.mall.service.UmsAdminService;
import cn.javgo.mall.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring Security 相关配置
 */
@Configuration
public class MallSecurityConfig {

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private UmsResourceService resourceService;

    @Bean
    public UserDetailsService userDetailsService() {
        // 获取登录用户信息
        return username -> adminService.loadUserByUsername(username);
    }

    @Bean
    public DynamicSecurityService dynamicSecurityService(){
        return () -> {
            // 使用 ConcurrentHashMap 保证线程安全
            Map<String,ConfigAttribute> map = new ConcurrentHashMap<>();
            // 获取所有资源
            List<UmsResource> resourceList = resourceService.listAll();
            // 将资源的 url 作为 key，将资源本身作为 value，这样就可以根据 url 查询到对应的资源了
            for (UmsResource resource : resourceList) {
                // 如 ： /brand/list -> 1:品牌管理
                map.put(resource.getUrl(),new SecurityConfig(resource.getId()+":"+resource.getName()));
            }
            return map;
        };
    }
}
