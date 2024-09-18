package org.naukma.spring.modulith.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.naukma.spring.modulith.review.dto.CreateReviewRequestDto;
import org.naukma.spring.modulith.review.dto.ReviewResponseDto;
import org.naukma.spring.modulith.review.model.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);
    ReviewResponseDto entityToResponseDto(ReviewEntity reviewEntity);
    ReviewEntity createRequestDtoToToEntity(CreateReviewRequestDto createReviewRequestDto);
}
