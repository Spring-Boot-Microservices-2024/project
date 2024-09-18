package org.naukma.spring.modulith.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);
    UserDto entityToDto(UserEntity userEntity);
    @Mapping(target = "id", ignore = true)
    UserEntity createRequestDtoToToEntity(CreateUserRequestDto createUserRequestDto);
    UserEntity dtoToToEntity(UserDto userDto);
}
