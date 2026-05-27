package com.ansari.distributed_lovable.account_service.service.impl;

import com.ansari.distributed_lovable.account_service.dto.auth.AuthResponse;
import com.ansari.distributed_lovable.account_service.dto.auth.LoginRequest;
import com.ansari.distributed_lovable.account_service.dto.auth.SignupRequest;
import com.ansari.distributed_lovable.account_service.entity.User;
import com.ansari.distributed_lovable.account_service.mapper.UserMapper;
import com.ansari.distributed_lovable.account_service.repository.UserRepository;
import com.ansari.distributed_lovable.account_service.service.AuthService;
import com.ansari.distributed_lovable.common_lib.error.BadRequestException;
import com.ansari.distributed_lovable.common_lib.security.AuthUtil;
import com.ansari.distributed_lovable.common_lib.security.JwtUserPrincipal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("Username already taken");
        });

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        JwtUserPrincipal jwtUserPrincipal = new JwtUserPrincipal(user.getId(),user.getName(), user.getUsername(), null,new ArrayList<>());

        String token  = authUtil.generateAccessToken(jwtUserPrincipal);

        return new AuthResponse(token,userMapper.toUserProfileResponse(jwtUserPrincipal));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        JwtUserPrincipal jwtUserPrincipal = (JwtUserPrincipal) authentication.getPrincipal();

//        JwtUserPrincipal jwtUserPrincipal = new JwtUserPrincipal(user.getId(),user.getName(), user.getUsername(), null,new ArrayList<>());
//

        String token  = authUtil.generateAccessToken(jwtUserPrincipal);

        return new AuthResponse(token,userMapper.toUserProfileResponse(jwtUserPrincipal));
    }
}
