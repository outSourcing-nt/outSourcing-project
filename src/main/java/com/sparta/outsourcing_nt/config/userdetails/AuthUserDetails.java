package com.sparta.outsourcing_nt.config.userdetails;

import com.sparta.outsourcing_nt.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class AuthUserDetails implements UserDetails {
    private final User user; // User 엔티티 포함
    private final Collection<? extends GrantedAuthority> authorities;

    // 생성자
    public AuthUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // User 엔티티의 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // User 엔티티의 이메일 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }
}