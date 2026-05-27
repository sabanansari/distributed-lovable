package com.ansari.distributed_lovable.intelligence_service.service;

import com.ansari.distributed_lovable.intelligence_service.dto.chat.StreamResponse;
import reactor.core.publisher.Flux;

public interface AIGenerationService {
    Flux<StreamResponse> streamResponse(String message, Long projectId);
}
