package org.naukma.spring.modulith.user;

public interface IUserService {
    UserDto createUser(CreateUserRequestDto user);
    void deleteUser(Long userId);
    UserDto getUserById(Long userId);
    UserDto getUserByUsername(String username);
    UserDto getUserByEmail(String email);
}
