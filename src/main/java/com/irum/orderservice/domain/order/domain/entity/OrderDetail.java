package com.irum.orderservice.domain.order.domain.entity;

import com.irum.global.domain.BaseEntity;
import com.irum.orderservice.domain.client.product.dto.response.ProductInternalResponse;
import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
@NoArgsConstructor
@Table(name = "p_order_detail")
public class OrderDetail extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(
            name = "order_detail_id",
            columnDefinition = "uuid",
            nullable = false,
            updatable = false)
    private UUID orderDetailId;

    private String optionName;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatusIndi;

    private String trackingNumber;

    private LocalDateTime arrivedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    // ManyToOne
    private UUID productOptionValueId;

    // ManyToOne
    private UUID productId;

    public void updateStatusToPreparing() {
        this.orderStatusIndi = OrderStatus.PREPARING;
    }

    public void updateStatusToShipped(String trackingNumber) {
        this.orderStatusIndi = OrderStatus.SHIPPED;
        this.trackingNumber = trackingNumber;
    }

    public void updateStatusToDelivered() {
        this.orderStatusIndi = OrderStatus.DELIVERED;
    }

    public void updateStatus(OrderStatus orderStatusIndi) {
        this.orderStatusIndi = orderStatusIndi;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public static OrderDetail from(
            ProductInternalResponse.ProductResponse product,
            int productPrice,
            int productQuantity) {
        return OrderDetail.builder()
                .productId(product.productId())
                .price(productPrice)
                .quantity(productQuantity)
                .orderStatusIndi(OrderStatus.PENDING)
                .optionName(product.optionName())
                .productName(product.productName())
                .productOptionValueId(product.optionValueId())
                .build();
    }
}
