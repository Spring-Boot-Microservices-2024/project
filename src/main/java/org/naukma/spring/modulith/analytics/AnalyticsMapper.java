package org.naukma.spring.modulith.analytics;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {
    AnalyticsMapper INSTANCE = Mappers.getMapper(AnalyticsMapper.class);

    AnalyticsDto entityToDto(AnalyticsEntity analyticsEntity);

    AnalyticsEntity dtoToEntity(AnalyticsDto analyticsDto);
}
