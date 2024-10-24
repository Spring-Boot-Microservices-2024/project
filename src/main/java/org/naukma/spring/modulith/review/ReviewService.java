package org.naukma.spring.modulith.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.naukma.spring.modulith.analytics.AnalyticsService;
import org.naukma.spring.modulith.event.DeletedEventEvent;
import org.naukma.spring.modulith.event.EventDto;
import org.naukma.spring.modulith.event.EventMapper;
import org.naukma.spring.modulith.event.EventService;
import org.naukma.spring.modulith.user.DeletedUserEvent;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserMapper;
import org.naukma.spring.modulith.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AnalyticsService analyticsService;
    private final UserService userService;
    private final EventService eventService;

    public ReviewDto getReviewById(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));
        return ReviewMapper.INSTANCE.entityToResponseDto(review);
    }

    public void deleteReviewById(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            log.info("Review with ID: {} has been deleted successfully", reviewId);
        } else {
            log.warn("Review not found for deletion with ID: {}", reviewId);
        }
    }

    @Transactional
    public ReviewDto createReview(CreateReviewRequestDto reviewRequestDto) {
        UserDto user = userService.getUserById(reviewRequestDto.getUserId());
        EventDto event = eventService.getEventById(reviewRequestDto.getEventId());
        ReviewEntity savedReview = ReviewMapper.INSTANCE.createRequestDtoToToEntity(reviewRequestDto);
        savedReview.setAuthor(UserMapper.INSTANCE.dtoToEntity(user));
        savedReview.setEvent(EventMapper.INSTANCE.dtoToEntity(event));
        savedReview = reviewRepository.save(savedReview);
        analyticsService.reportEvent(AnalyticsEventType.REVIEW_CREATED);
        log.info("Review with ID: {} has been created successfully", savedReview.getId());

        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.REVIEW_CREATED));

        return ReviewMapper.INSTANCE.entityToResponseDto(savedReview);
    }

    public List<ReviewDto> getAllReviews() {
        List<ReviewEntity> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(ReviewMapper.INSTANCE::entityToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getAllReviewsByAuthorId(Long authorId) {
        List<ReviewEntity> reviews = reviewRepository.findAllByAuthorId(authorId);
        return reviews.stream()
                .map(ReviewMapper.INSTANCE::entityToResponseDto)
                .collect(Collectors.toList());
    }

    @EventListener
    public void onDeletedUserEvent(DeletedUserEvent event) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByAuthorId(event.getUserId());
        reviewRepository.deleteAll(reviewEntities);
    }

    @EventListener
    public void onDeletedEventEvent(DeletedEventEvent event) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByEventId(event.getEventId());
        reviewRepository.deleteAll(reviewEntities);
    }
}
