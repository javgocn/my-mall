package cn.javgo.mall.bo;

import cn.javgo.mall.model.UmsAdmin;
import cn.javgo.mall.model.UmsResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security 需要的用户信息封装类
 */
public class AdminUserDetails implements UserDetails {

    private final UmsAdmin umsAdmin;

    private final List<UmsResource> resourceList;

    public AdminUserDetails(UmsAdmin umsAdmin, List<UmsResource> resourceList) {
        this.umsAdmin = umsAdmin;
        this.resourceList = resourceList;
    }

    /**
     * 返回当前用户所具有的资源列表 (即资源表的 id + name，如 1:商品管理)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return resourceList.stream()
                .map(role -> new SimpleGrantedAuthority(role.getId() + ":" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 账户是否未过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 账户是否未锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 密码是否未过期
    }

    @Override
    public boolean isEnabled() {
        return umsAdmin.getStatus().equals(1); // 账户是否可用
    }
}
