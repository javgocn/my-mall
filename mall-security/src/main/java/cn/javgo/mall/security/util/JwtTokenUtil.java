package cn.javgo.mall.security.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAM_KEY_USERNAME = "sub";
    private static final String CLAM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    //================ private methods ==================

    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    private Date generateExpirationDate(){
        /*
            Date 构造器接受格林威治时间，推荐使用 System.currentTimeMillis() 获取当前时间距离 1970-01-01 00:00:00 的毫秒数
            而我们在配置文件中配置的是秒数，所以需要乘以 1000。一般而言 Token 的过期时间为 7 天，因此我们一般在 Spring Boot 的
            配置文件中将 jwt.expiration 设置为 604800，即 7 * 24 * 3600。
         */
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Claims getClaimsFromToken(String token){
        Claims claims = null;
        try{
            claims = Jwts.parser()
                    // 指定签名使用的密钥
                    .setSigningKey(secret)
                    // 传入要解析的 jwt 进行验证
                    .parseClaimsJws(token)
                    // 获取载荷部分
                    .getBody();
        }catch (Exception e){
            LOGGER.info("JWT 格式验证失败：{}",token);
        }
        return claims;
    }

    private boolean isTokenExpired(String token){
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    private Date getExpiredDateFromToken(String token){
        return getClaimsFromToken(token).getExpiration();
    }

    private boolean tokenRefreshJustBefore(String token,int time){
        Claims claims = getClaimsFromToken(token);
        Date tokenCreateDate = claims.get(CLAM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        // 刷新时间在创建时间之后 + 刷新时间在创建时间后的指定时间段内（默认为 30 分钟）
        if (refreshDate.after(tokenCreateDate) && refreshDate.before(DateUtil.offsetSecond(tokenCreateDate,time))){
            return true;
        }
        return false;
    }

    //================ public methods ==================

    public String getUserNameFromToken(String token){
        String username;
        try{
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUserNameFromToken(token);
        // 需要同时满足：当前用户的名户名与 token 中的用户名相同，且 token 未过期
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    public String refreshHeadToken(String oldToken){
        // token 为 null || length == 0
        if (StrUtil.isEmpty(oldToken)) return null;
        // token 不为 null 但是没有携带实际的 token 内容
        String token = oldToken.substring(tokenHead.length());
        if (StrUtil.isEmpty(token)) return null;
        // token 校验失败
        Claims claims = getClaimsFromToken(oldToken);
        if (claims == null) return null;
        // token 已过期
        if (isTokenExpired(oldToken)) return null;

        // 如果 token 在 30 分钟之内刚刷新过，返回原 token
        if (tokenRefreshJustBefore(oldToken,30*60)){
            return token;
        }else {
            // 否则重新生成 token
            claims.put(CLAM_KEY_CREATED,new Date());
            return generateToken(claims);
        }

    }
}
