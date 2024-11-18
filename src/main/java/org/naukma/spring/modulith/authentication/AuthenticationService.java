package org.naukma.spring.modulith.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;

    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic "))
            return Optional.empty();

        String[] auth = new String(Base64.getDecoder().decode(authHeader.substring(6))).split(":");
        String email = auth[0];
        String password = auth[1];

        if (email == null || password == null)
            return Optional.empty();

        UserDto user = userService.getUserForAuth(email, password);

        System.out.println("User: " + user);

        if (user == null)
            return Optional.empty();

        return Optional.of(new UserAuthentication(user, AuthorityUtils.createAuthorityList("USER")));
    }
}
