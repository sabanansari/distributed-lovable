package com.ansari.distributed_lovable.intelligence_service.dto.chat;


import com.ansari.distributed_lovable.common_lib.enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        Integer sequenceOrder,
        String content,
        String filePath,
        ChatEventType type,
        String metadata
) {
}
