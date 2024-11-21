package org.naukma.spring.modulith.authentication;

import org.naukma.spring.modulith.user.UserDto;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAuthentication that) || !super.equals(o)) return false;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}
