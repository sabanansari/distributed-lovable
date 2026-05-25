package com.ansari.distributed_lovable.account_service.service;


import com.ansari.distributed_lovable.account_service.dto.subscription.SubscriptionResponse;
import com.ansari.distributed_lovable.common_lib.dto.PlanDto;
import com.ansari.distributed_lovable.common_lib.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();

    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String gatewaySubscriptionId);

    void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd);

    void markSubscriptionAsPastDue(String subId);

    PlanDto getCurrentSubscribedPlanByUser();

    // boolean canCreateProject();
}
