package com.ansari.distributed_lovable.intelligence_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic fileStorageEvent() {
        return new NewTopic("file-storage-request-event", 3, (short) 1);
    }

}
