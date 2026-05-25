package com.ansari.distributed_lovable.account_service.mapper;

import com.ansari.distributed_lovable.account_service.dto.subscription.SubscriptionResponse;
import com.ansari.distributed_lovable.account_service.entity.Subscription;
import com.ansari.distributed_lovable.common_lib.dto.PlanDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanDto toPlanDto(PlanDto plan);
}
