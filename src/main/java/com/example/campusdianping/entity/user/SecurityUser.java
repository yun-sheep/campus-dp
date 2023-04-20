package com.example.campusdianping.entity.user;

import com.example.campusdianping.entity.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @Description 权限认证用户实体
 * @auther j2-yizhiyang
 * @date 2023/4/3 20:09
 */
@Data
public class SecurityUser implements UserDetails {
    public Long id;
    public String nickname;
    public String icon;
    public SecurityUser(User user){
        this.nickname = user.getNickName();
        this.icon = user.getIcon();
        this.id = user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
