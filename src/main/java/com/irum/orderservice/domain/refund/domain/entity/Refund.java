package com.irum.orderservice.domain.refund.domain.entity;

import com.irum.global.domain.BaseEntity;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.refund.domain.entity.enums.RefundReason;
import com.irum.orderservice.domain.refund.domain.entity.enums.RefundStatus;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
@NoArgsConstructor
@Builder
@Getter
@Table(name = "p_refund")
public class Refund extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "refund_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID refundId;

    @Enumerated(EnumType.STRING)
    private RefundReason reason;

    @Lob private String description;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus refundStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static Refund create(
            RefundReason reason, String description, Order order, int refundPrice) {
        return Refund.builder()
                .reason(reason)
                .description(description)
                .price(order.getPayingAmount())
                .refundStatus(RefundStatus.PENDING)
                .order(order)
                .build();
    }

    public void updateStatus(RefundStatus newStatus) {
        this.refundStatus = newStatus;
    }
}
