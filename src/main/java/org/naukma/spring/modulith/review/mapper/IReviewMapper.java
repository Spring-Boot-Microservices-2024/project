package org.naukma.spring.modulith.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.naukma.spring.modulith.review.model.ReviewEntity;
import org.naukma.spring.modulith.review.dto.CreateReviewRequestDto;
import org.naukma.spring.modulith.review.dto.ReviewResponseDto;

@Mapper(componentModel = "spring")
public interface IReviewMapper {
    IReviewMapper INSTANCE = Mappers.getMapper(IReviewMapper.class);
    ReviewResponseDto entityToResponseDto(ReviewEntity reviewEntity);
    ReviewEntity createRequestDtoToToEntity(CreateReviewRequestDto createReviewRequestDto);
}
