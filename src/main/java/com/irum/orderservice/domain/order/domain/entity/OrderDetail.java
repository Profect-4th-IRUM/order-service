package com.irum.orderservice.domain.order.domain.entity;

import com.irum.orderservice.domain.client.product.dto.response.ProductInternalResponse;
import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import com.irum.orderservice.global.domain.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted_at is null")
@Table(name = "p_order_detail")
public class OrderDetail extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "order_detail_id", nullable = false, updatable = false)
    private UUID orderDetailId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "option_value_id", nullable = false)
    private UUID optionValueId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "option_name")
    private String optionName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status_indi", nullable = false)
    private OrderStatus orderStatusIndi;

    private String trackingNumber;
    private LocalDateTime arrivedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    // develop 쪽에서 추가된 필드 통합
    // (컬럼명은 DB 설계에 맞게 필요하면 name 지정)
    private UUID productOptionValueId;

    // ✔ feature/#18 쪽에서 추가한 팩토리 메서드 살림
    public static OrderDetail create(
            Order order,
            UUID productId,
            UUID optionValueId,
            Long memberId,
            String productName,
            String optionName,
            Integer price,
            Integer quantity,
            OrderStatus status) {
        return OrderDetail.builder()
                .order(order)
                .productId(productId)
                .optionValueId(optionValueId)
                .memberId(memberId)
                .productName(productName)
                .optionName(optionName)
                .price(price)
                .quantity(quantity)
                .orderStatusIndi(status)
                .build();
    }

    public void updateStatus(OrderStatus newStatus) {
        this.orderStatusIndi = newStatus;
    }

    public void updateStatusToShipped(String trackingNumber) {
        this.orderStatusIndi = OrderStatus.SHIPPED;
        this.trackingNumber = trackingNumber;
    }

    public void updateStatusToDelivered() {
        this.orderStatusIndi = OrderStatus.DELIVERED;
        this.arrivedDate = LocalDateTime.now();
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
                .optionValueId(product.optionValueId())
                .productOptionValueId(product.optionValueId())
                .price(productPrice)
                .quantity(productQuantity)
                .orderStatusIndi(OrderStatus.PENDING)
                .optionName(product.optionName())
                .productName(product.productName())
                .build();
    }
}
