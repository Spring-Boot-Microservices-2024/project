package org.naukma.spring.modulith.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IEventMapper {
    IEventMapper INSTANCE = Mappers.getMapper(IEventMapper.class);
    EventDto entityToDto(EventEntity eventEntity);
    EventEntity dtoToEntity(EventDto eventDto);
    @Mapping(target = "id", ignore = true)
    EventEntity createRequestDtoToEntity(CreateEventRequestDto createEventRequestDto);
}
