package org.naukma.spring.modulith.review;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);
    ReviewDto entityToResponseDto(ReviewEntity reviewEntity);
    @Mapping(target = "id", ignore = true)
    ReviewEntity createRequestDtoToToEntity(CreateReviewRequestDto createReviewRequestDto);
}
