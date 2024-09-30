package org.naukma.spring.modulith.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.naukma.spring.modulith.analytics.AnalyticsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AnalyticsService analyticsService;

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto createUser(CreateUserRequestDto user) {
        log.info("Creating physical user");
        UserEntity createdUser = userRepository.save(UserMapper.INSTANCE.createRequestDtoToToEntity(user));
        analyticsService.reportEvent(AnalyticsEventType.USER_REGISTERED);
        log.info("User created successfully.");

        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.USER_REGISTERED));

        return UserMapper.INSTANCE.entityToDto(createdUser);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            eventPublisher.publishEvent(new DeletedUserEvent(userId));
            userRepository.deleteById(userId);
            log.info("Deleted user with ID: {}", userId);
        } else {
            log.warn("User not found for deletion with ID: {}", userId);
        }
    }

    public UserDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return UserMapper.INSTANCE.entityToDto(user);
    }

    public UserDto getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return UserMapper.INSTANCE.entityToDto(user);
    }

    @Cacheable("users")
    public List<UserDto> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();
        log.info("Returning all users from database");
        return users.stream().map(UserMapper.INSTANCE::entityToDto).toList();
    }
}
