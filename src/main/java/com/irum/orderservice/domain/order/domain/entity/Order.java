package com.irum.orderservice.domain.order.domain.entity;

import com.irum.global.domain.BaseEntity;
import com.irum.orderservice.domain.deliveryaddress.domain.entity.DeliveryAddress;
import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
@Table(name = "p_order")
public class Order extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "order_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID orderId;

    @Column(nullable = false)
    private String orderNum;

    private Integer totalPrice;

    private Integer deliveryFee;

    private String deliveryRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatusAll;

    // OneToOne
    private UUID paymentId;

    // ManyToOne
    private Long memberId;

    // ManyToOne
    private UUID storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id")
    private DeliveryAddress deliveryAddress;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // 한 주문당 여러 상품이 담겨있기 때문에 List 추가
    public void updateOrderStatus(OrderStatus os) {
        this.orderStatusAll = os;
    }

    public static Order from(
            String orderNum,
            int calculatedTotalPrice,
            Integer deliveryFee,
            String deliveryRequest,
            Long memberId,
            UUID storeId,
            UUID paymentId,
            DeliveryAddress deliveryAddress) {
        return Order.builder()
                .orderNum(orderNum)
                .totalPrice(calculatedTotalPrice)
                .deliveryFee(deliveryFee)
                .deliveryRequest(deliveryRequest)
                .orderStatusAll(OrderStatus.PENDING)
                .memberId(memberId)
                .storeId(storeId)
                .paymentId(paymentId)
                .deliveryAddress(deliveryAddress)
                .build();
    }
}
