package org.naukma.spring.modulith.review;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.utils.ExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@RequestBody @Valid CreateReviewRequestDto reviewRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        ReviewDto createdReview = reviewService.createReview(reviewRequestDto);
        log.info("Review created with ID: {}", createdReview.getId());
        return createdReview;
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReviewById(@PathVariable Long reviewId) {
        log.info("Deleting review with ID: {}", reviewId);
        reviewService.deleteReviewById(reviewId);
    }

    @GetMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto getReviewById(@PathVariable Long reviewId) {
        log.debug("Retrieving review with ID: {}", reviewId);
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getAll() {
        log.debug("Retrieving all reviews");
        return reviewService.getAllReviews();
    }

    @GetMapping("/author/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getAllByParticipantId(@PathVariable Long authorId) {
        log.debug("Retrieving all reviews for author with ID {}", authorId);
        return reviewService.getAllReviewsByAuthorId(authorId);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<String> handleNoSuchCourseException(ReviewNotFoundException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
