package com.irum.orderservice.domain.order.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_order_outbox")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOutbox {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "order_outbox_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "aggregate_type", nullable = false) // Debezium 표준 컬럼명: aggregatetype
    private String aggregateType; // 어느 table 발생했는지

    @Column(name = "aggregate_id", nullable = false) // Debezium 표준 컬럼명: aggregateid
    private String aggregateId; // table id

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
