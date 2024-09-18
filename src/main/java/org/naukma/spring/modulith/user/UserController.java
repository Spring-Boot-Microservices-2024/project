package org.naukma.spring.modulith.user;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/new")
    public UserDto createUser(@RequestBody UserDto user) {
        UserDto createdUser = userService.createUser(user);
        log.info("User created with ID: {}", createdUser.getId());
        return createdUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.info("UserEntity deleted with ID: {}", id);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Retrieving user with ID: {}", userId);
        Optional<UserDto> user = userService.getUserById(userId);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId);
        return user.get();
    }

    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        log.info("Retrieving user with username: {}", username);
        return userService.getUserByUsername(username);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        if (e instanceof ConstraintViolationException)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        else if (e instanceof ResponseStatusException)
            return new ResponseEntity<>(e.getMessage(), ((ResponseStatusException) e).getStatusCode());

        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping()
    public List<UserDto> getAllUsers() {
        log.info("Retrieving all users");
        return userService.getAllUsers();
    }
}
