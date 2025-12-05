package com.irum.orderservice.domain.order.domain.repository;

import com.irum.orderservice.domain.order.domain.entity.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, UUID> {
}
