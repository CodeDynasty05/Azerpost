package com.azerpost.app.mapper;

import com.azerpost.app.model.entity.User;
import com.azerpost.app.model.dto.SignupRequest;
import com.azerpost.app.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)  // Ignores nulls in source
public interface UserMapper {

    UserDto userToUserDto(User user);

    void updateUserFromDto(SignupRequest request, @MappingTarget User user);
}
