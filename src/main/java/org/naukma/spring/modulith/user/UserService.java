package org.naukma.spring.modulith.user;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserRequestDto user);
    void deleteUser(Long userId);
    UserDto getUserById(Long userId);
    UserDto getUserByEmail(String email);
    List<UserDto> getAllUsers();
}
