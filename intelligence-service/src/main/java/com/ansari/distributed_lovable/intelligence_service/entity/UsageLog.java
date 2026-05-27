package com.ansari.distributed_lovable.intelligence_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="usage_logs",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","date"})})
public class UsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="user_id", nullable = false)
    Long userId;

    @Column(nullable = false)
    LocalDate date; // Format: YYYY-MM-DD

    Integer tokensUsed;

}
