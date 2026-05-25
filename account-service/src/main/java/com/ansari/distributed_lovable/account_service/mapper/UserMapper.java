package com.ansari.distributed_lovable.account_service.mapper;


import com.ansari.distributed_lovable.account_service.dto.auth.SignupRequest;
import com.ansari.distributed_lovable.account_service.dto.auth.UserProfileResponse;
import com.ansari.distributed_lovable.account_service.entity.User;
import com.ansari.distributed_lovable.common_lib.dto.UserDto;
import com.ansari.distributed_lovable.common_lib.security.JwtUserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignupRequest request);

    @Mapping(source="userId", target="id")
    UserProfileResponse toUserProfileResponse(JwtUserPrincipal user);

    UserDto toUserDto(User user);
}
