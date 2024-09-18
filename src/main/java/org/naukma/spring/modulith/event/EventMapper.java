package org.naukma.spring.modulith.event;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);
    EventDto entityToDto(EventEntity eventEntity);
    EventEntity dtoToEntity(EventDto eventDto);
    EventRequestDto dtoToRequestDto(EventDto eventDto);
    EventDto requestDtoToDto(EventRequestDto eventRequestDto);
}
