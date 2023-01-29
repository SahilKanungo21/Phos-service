package com.phosservice.Phosservice.Security.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@JsonDeserialize(as = SqlUserDetails.class)
@Setter
@AllArgsConstructor
public class SqlUserDetails implements UserDetails {
    private String username;
    private String password;

    private String fullName;

    private boolean active;

    private boolean isLocked;

    private boolean isExpired;

    private boolean isEnabled;

    private List<GrantedAuthority> grantedAuthorities;

    public SqlUserDetails(String username,String authorities, String fullName) {
        this.username = username;
        this.fullName = fullName;
        this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
    }

    public SqlUserDetails() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFullName() {
        return fullName;
    }
}
