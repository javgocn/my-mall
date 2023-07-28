package cn.javgo.mall.security.component;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * 动态权限相关业务接口，在本项目中是基于路径的动态权限控制，即根据前端传递的 URL，动态地返回该 URL 所需要的角色或权限。
 * 我们将以此项目中是否存在 DynamicSecurityService Bean 为判断依据。
 * 1.如果存在 DynamicSecurityService Bean，则说明开启了基于路径的动态权限控制，此时会在我们自定义的 FilterInvocationSecurityMetadataSource
 *   中调用 DynamicSecurityService 的 loadDataSource() 方法，获取所有的资源 ANT 通配符和资源对应 MAP，最终用于自定义的 DynamicAccessDecisionManager
 *   进行鉴权时使用。
 * 2.如果不存在 DynamicSecurityService Bean，则说明没有开启基于路径的动态权限控制，此时会直接调用默认的 AccessDecisionManager 的
 *   decide() 方法，进行鉴权。
 */
public interface DynamicSecurityService {

    /**
     * 加载资源 ANT 通配符和资源对应 MAP
     * 说明：
     * 1.对应的数据表为：ums_resource
     * 2.这里的资源指的是：前端请求的 URL，比如 /brand/list，/product/updateInfo 等
     * 3.这里的 Map 的 key 即数据库端存储的 ANT 风格的 URL，比如 /brand/list，/product/updateInfo 等
     * 4.ConfigAttribute 接口里面只有一个 getAttribute() 方法，返回值为 String 类型，即权限或角色的名称。该接口的常用实现类为
     *   SecurityConfig 类。该类的构造方法接收一个 String 类型的参数，即权限或角色的名称。
     */
    Map<String, ConfigAttribute> loadDataSource();
}
