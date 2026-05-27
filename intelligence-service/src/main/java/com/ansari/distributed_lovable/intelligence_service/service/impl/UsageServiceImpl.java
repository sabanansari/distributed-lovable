package com.ansari.distributed_lovable.intelligence_service.service.impl;

import com.ansari.distributed_lovable.common_lib.dto.PlanDto;
import com.ansari.distributed_lovable.common_lib.security.AuthUtil;
import com.ansari.distributed_lovable.intelligence_service.client.AccountClient;
import com.ansari.distributed_lovable.intelligence_service.entity.UsageLog;
import com.ansari.distributed_lovable.intelligence_service.repository.UsageLogRepository;
import com.ansari.distributed_lovable.intelligence_service.service.UsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsageServiceImpl implements UsageService {

    private final UsageLogRepository usageLogRepository;
    private final AuthUtil authUtil;
    private final AccountClient accountClient;

    @Override
    public void recordTokenUsage(Long userId, int actualTokens) {
        LocalDate today = LocalDate.now();

        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> createNewDailyLog(userId, today));

        todayLog.setTokensUsed(todayLog.getTokensUsed() + actualTokens);
        usageLogRepository.save(todayLog);
    }

    @Override
    public void checkDailyTokensUsage() {

        Long userId = authUtil.getCurrentUserId();

        PlanDto plan = accountClient.getCurrentSubscriptionPlanByUser();

        LocalDate today = LocalDate.now();

        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> createNewDailyLog(userId, today));

        int currentUsage = todayLog.getTokensUsed();
        int limit = plan.maxTokensPerDay();

        if(currentUsage >= limit){
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Daily token usage limit exceeded. Please upgrade your plan for more usage.");
        }

    }

    private UsageLog createNewDailyLog(Long userId, LocalDate date){
        UsageLog newLog = UsageLog.builder()
                .userId(userId)
                .date(date)
                .tokensUsed(0)
                .build();
        return usageLogRepository.save(newLog);

    }
}
