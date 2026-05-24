package com.ansari.distributed_lovable.account_service.service;


import com.ansari.distributed_lovable.account_service.dto.auth.AuthResponse;
import com.ansari.distributed_lovable.account_service.dto.auth.LoginRequest;
import com.ansari.distributed_lovable.account_service.dto.auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
