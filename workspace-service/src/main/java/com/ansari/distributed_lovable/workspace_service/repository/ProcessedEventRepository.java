package com.ansari.distributed_lovable.workspace_service.repository;


import com.ansari.distributed_lovable.workspace_service.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent,String> {
}
