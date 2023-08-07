package com.example.OrderService.model;

import java.time.Instant;

import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.stdDSA;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;
    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

// Inner Class
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public static class ProductDetails {


    private long productId;
    private String productName;
    private long price;
    private long quantity;
    
}


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public static class PaymentDetails {

    private long paymentId;
    private String paymentStatus;
    private PaymentMode paymentMode;
    private Instant paymentDate;
    
}
}
    
