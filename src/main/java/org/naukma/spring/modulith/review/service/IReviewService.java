package org.naukma.spring.modulith.review.service;

import org.naukma.spring.modulith.review.dto.CreateReviewRequestDto;
import org.naukma.spring.modulith.review.dto.ReviewResponseDto;

import java.util.List;

public interface IReviewService {
    ReviewResponseDto getReviewById(Long reviewId);
    void deleteReviewById(Long reviewId);
    ReviewResponseDto createReview(CreateReviewRequestDto reviewRequestDto);
    List<ReviewResponseDto> getAllReviews();
    List<ReviewResponseDto> getAllReviewsByAuthorId(Long authorId);
}
