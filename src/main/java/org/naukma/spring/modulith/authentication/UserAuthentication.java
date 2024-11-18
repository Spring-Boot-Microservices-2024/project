package org.naukma.spring.modulith.authentication;

import org.naukma.spring.modulith.user.UserDto;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication extends AbstractAuthenticationToken {
    private final UserDto user;

    public UserAuthentication(UserDto user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
