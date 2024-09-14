package org.naukma.spring.modulith.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.naukma.spring.modulith.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Transactional
    public UserDto createUser(UserDto user) {
        logger.info("Creating physical user");
        UserEntity createdUser = userRepository.save(IUserMapper.INSTANCE.dtoToEntity(user));
        logger.info("User created successfully.");
        return IUserMapper.INSTANCE.entityToDto(createdUser);
    }

    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            eventPublisher.publishEvent(new DeletedUserEvent(userId));
            userRepository.deleteById(userId);
            logger.info("Deleted user with ID: {}", userId);
        } else {
            logger.warn("User not found for deletion with ID: {}", userId);
        }
    }

    public UserDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            logger.info("Retrieved user with ID: {}", user.getId());
        } else {
            logger.warn("User not found with ID: {}", userId);
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
        return IUserMapper.INSTANCE.entityToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return user.map(IUserMapper.INSTANCE::entityToDto).orElse(null);
    }

    public UserDto getUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.map(IUserMapper.INSTANCE::entityToDto).orElse(null);
    }
}
