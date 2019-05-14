package com.example.demo.core.userdetails;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public interface UserDetails extends Serializable {

    Collection<? extends GrantedAuthority> getAuthorities();
    String getPassword();
    String getUsername();
    boolean isAccountNonExpired();
    boolean isAccoutnNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();
}
