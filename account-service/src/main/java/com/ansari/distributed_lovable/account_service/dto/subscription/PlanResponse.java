package com.ansari.distributed_lovable.account_service.dto.subscription;

public record PlanResponse(

        Long id,

        String name,

        Integer maxProjects,
        Integer maxTokenPerDay,
        Boolean unlimitedAi,
        String price

) {
}
