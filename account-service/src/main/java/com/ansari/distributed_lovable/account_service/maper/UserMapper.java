package com.ansari.distributed_lovable.account_service.maper;

import com.ansari.distributed_lovable.account_service.dto.auth.SignupRequest;
import com.ansari.distributed_lovable.account_service.dto.auth.UserProfileResponse;
import com.ansari.distributed_lovable.account_service.entity.User;
import com.ansari.distributed_lovable.common_lib.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignupRequest request);

    UserProfileResponse toUserProfileResponse(User user);

    UserDto toUserDto(User user);
}
