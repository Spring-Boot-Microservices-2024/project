package org.naukma.spring.modulith.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/new")
    public UserRequestDto createUser(@RequestBody @Valid UserRequestDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        UserDto createdUser = userService.createUser(IUserMapper.INSTANCE.requestDtoToDto(user));
        logger.info("User created with ID: {}", createdUser.getId());
        return IUserMapper.INSTANCE.dtoToRequestDto(createdUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        logger.info("UserEntity deleted with ID: {}", id);
    }

    @GetMapping("/{userId}")
    public UserRequestDto getUserById(@PathVariable Long userId) {
        logger.info("Retrieving user with ID: {}", userId);
        return IUserMapper.INSTANCE.dtoToRequestDto(userService.getUserById(userId));
    }

    @GetMapping("/username/{username}")
    public UserRequestDto getUserByUsername(@PathVariable String username) {
        logger.info("Retrieving user with username: {}", username);
        return IUserMapper.INSTANCE.dtoToRequestDto(userService.getUserByUsername(username));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        logger.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
