package org.naukma.spring.modulith.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto createUser(UserDto user) {
        log.info("Creating physical user");
        UserEntity createdUser = userRepository.save(IUserMapper.INSTANCE.dtoToEntity(user));
        log.info("User created successfully.");

        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.USER_REGISTERED));

        return IUserMapper.INSTANCE.entityToDto(createdUser);
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

    public Optional<UserDto> getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);

        if (user != null)
            return Optional.of(IUserMapper.INSTANCE.entityToDto(user));
        else
            return Optional.empty();
    }

    public UserDto getUserByUsername(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return user.map(IUserMapper.INSTANCE::entityToDto).orElse(null);
    }

    public UserDto getUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.map(IUserMapper.INSTANCE::entityToDto).orElse(null);
    }

    @Cacheable("users")
    public List<UserDto> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(IUserMapper.INSTANCE::entityToDto).toList();
    }
}
