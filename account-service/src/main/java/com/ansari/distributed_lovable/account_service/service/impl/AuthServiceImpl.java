package com.ansari.distributed_lovable.account_service.service.impl;

import com.ansari.distributed_lovable.account_service.dto.auth.AuthResponse;
import com.ansari.distributed_lovable.account_service.dto.auth.LoginRequest;
import com.ansari.distributed_lovable.account_service.dto.auth.SignupRequest;
import com.ansari.distributed_lovable.account_service.entity.User;
import com.ansari.distributed_lovable.account_service.maper.UserMapper;
import com.ansari.distributed_lovable.account_service.repository.UserRepository;
import com.ansari.distributed_lovable.account_service.service.AuthService;
import com.ansari.distributed_lovable.common_lib.error.BadRequestException;
import com.ansari.distributed_lovable.common_lib.security.AuthUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("Username already taken");
        });

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        String token  = authUtil.generateAccessToken(userMapper.toUserDto(user));

        return new AuthResponse(token,userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = (User) authentication.getPrincipal();

        String token  = authUtil.generateAccessToken(userMapper.toUserDto(user));

        return new AuthResponse(token,userMapper.toUserProfileResponse(user));
    }
}
