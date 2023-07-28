package cn.javgo.mall.security.aspect;

import cn.javgo.mall.security.annotation.CacheException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Redis 缓存切面，防止 Redis 宕机影响正常业务逻辑，因此使用该切面统一处理所有 Redis 缓存失败抛出的异常。这样即使 Redis 宕机，也不会影响正常业务逻辑。
 * 但是值得注意的是，并不是所有的方法都需要处理异常，比如 Redis 缓存验证码，如果 Redis 宕机，那么验证码就无法正常生成，这样就会影响正常业务逻辑。
 * 这时候我们需要的是显示的报错信息，而不是切面统一处理异常，返回成功。
 * 对此，我们自定义了一个 CacheException 注解，当切面执行时会先判断该方法上是否有 CacheException 注解，如果有，则不会统一处理异常，而是直接抛出异常。
 */
@Aspect
@Component
@Order(2) // 该切面的优先级为 2，因为需要放在记录日志的切面之后执行，否则会导致日志记录失败。
public class RedisCacheAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

    @Pointcut("execution(public * cn.javgo.mall.portal.service.*CacheService.*(..)) || execution(public * cn.javgo.mall.service.*CacheService.*(..))")
    public void cacheAspect() {
    }

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取切点的方法签名
        Signature signature = joinPoint.getSignature();
        // 转为 MethodSignature 对象，其中的 getMethod() 方法可以获取到当前执行的方法
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        // 记录返回结果
        Object result = null;
        try{
            // 执行切点方法
            result = joinPoint.proceed();
        }catch (Throwable throwable){
            // 有 CacheException 注解的方法需要抛出异常
            if (method.isAnnotationPresent(CacheException.class)){
                throw throwable;
            }else { // 否则记录异常信息，不影响正常业务逻辑
                LOGGER.error(throwable.getMessage());
            }
        }
        return result;
    }
}
