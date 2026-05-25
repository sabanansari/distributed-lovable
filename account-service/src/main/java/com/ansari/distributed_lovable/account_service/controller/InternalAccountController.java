package com.ansari.distributed_lovable.account_service.controller;

import com.ansari.distributed_lovable.account_service.mapper.UserMapper;
import com.ansari.distributed_lovable.account_service.repository.UserRepository;
import com.ansari.distributed_lovable.account_service.service.SubscriptionService;
import com.ansari.distributed_lovable.common_lib.dto.PlanDto;
import com.ansari.distributed_lovable.common_lib.dto.UserDto;
import com.ansari.distributed_lovable.common_lib.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1")
public class InternalAccountController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SubscriptionService subscriptionService;

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id){
        return userRepository.findById(id)
                        .map(userMapper::toUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found",id.toString()));
    }

    @GetMapping("/users/by-email")
    public Optional<UserDto> getUserByEmail(@RequestParam String email){
        return userRepository.findByUsernameIgnoreCase(email)
                .map(userMapper::toUserDto);
    }

    @GetMapping("/billing/current-plan")
    public PlanDto getCurrentSubscriptionPlan(){
        return subscriptionService.getCurrentSubscribedPlanByUser();
    }


}
