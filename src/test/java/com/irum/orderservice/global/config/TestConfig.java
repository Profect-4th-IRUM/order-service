package com.irum.orderservice.global.config;

import com.irum.global.infrastructure.config.GlobalAutoConfiguration;
import com.irum.orderservice.domain.deliveryaddress.service.DeliveryAddressService;
import com.irum.orderservice.domain.order.service.CustomerOrderService;
import com.irum.orderservice.domain.order.service.OwnerOrderService;
import com.irum.orderservice.domain.order.service.SalesService;
import com.irum.orderservice.domain.refund.service.RefundService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(GlobalAutoConfiguration.class) // 얘 붙이세요
public class TestConfig {
    @Bean
    public DeliveryAddressService deliveryAddressService() {
        return Mockito.mock(DeliveryAddressService.class);
    }

    @Bean
    public RefundService refundService() {
        return Mockito.mock(RefundService.class);
    }

    public OwnerOrderService ownerOrderService() {
        return Mockito.mock(OwnerOrderService.class);
    }

    @Bean
    public CustomerOrderService customerOrderService() {
        return Mockito.mock(CustomerOrderService.class);
    }

    @Bean
    public SalesService salesService() {
        return Mockito.mock(SalesService.class);
    }
}
