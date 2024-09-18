package org.naukma.spring.modulith.review;

import java.util.List;

public interface IReviewService {
    ReviewDto getReviewById(Long reviewId);
    void deleteReviewById(Long reviewId);
    ReviewDto createReview(CreateReviewRequestDto reviewRequestDto);
    List<ReviewDto> getAllReviews();
    List<ReviewDto> getAllReviewsByAuthorId(Long authorId);
}
