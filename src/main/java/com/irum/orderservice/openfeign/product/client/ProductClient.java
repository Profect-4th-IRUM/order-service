//package com.irum.orderservice.openfeign.product.client;
//
//import com.irum.orderservice.openfeign.product.dto.request.ProductInternalRequest;
//import com.irum.orderservice.openfeign.product.dto.request.RollbackStockRequest;
//import com.irum.orderservice.openfeign.product.dto.response.ProductInternalResponse;
//import com.irum.orderservice.openfeign.product.dto.response.StoreResponse;
//import java.util.UUID;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient(name = "PRODUCT-SERVICE", url = "/internal")
//public interface ProductClient {
//
//    /** 재고 롤백 */
//    @PatchMapping("/products/rollback")
//    void rollbackStock(@RequestBody RollbackStockRequest request);
//
//    /** 주문 - 재고 차감 */
//    @GetMapping("/products/stock")
//    ProductInternalResponse updateStock(@RequestBody ProductInternalRequest request);
//
//    @GetMapping("/stores/{storeId}/owner")
//    StoreResponse getStoreId(@PathVariable UUID storeId);
//}
