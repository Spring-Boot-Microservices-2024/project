package org.naukma.spring.modulith.review;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IReviewMapper {
    IReviewMapper INSTANCE = Mappers.getMapper(IReviewMapper.class);
    ReviewDto entityToResponseDto(ReviewEntity reviewEntity);
    @Mapping(target = "id", ignore = true)
    ReviewEntity createRequestDtoToToEntity(CreateReviewRequestDto createReviewRequestDto);
}
