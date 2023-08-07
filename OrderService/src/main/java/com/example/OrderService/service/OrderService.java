package com.example.OrderService.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.OrderService.entity.Order;
import com.example.OrderService.exception.CustomException;
import com.example.OrderService.external.client.PaymentService;
import com.example.OrderService.external.client.ProductService;
import com.example.OrderService.external.request.PaymentRequest;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.model.OrderResponse;
import com.example.OrderService.model.PaymentResponse;
import com.example.OrderService.model.ProductResponse;
import com.example.OrderService.repository.OrderRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;


    @Value("${microservices.product}")
    private String productServiceUrl;
    @Value("${microservices.payment}")
    private String paymentServiceUrl;

    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order Request:{}",orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order With status Created");

        Order order=Order.builder()
        .amount(orderRequest.getTotalAmount())
        .orderStatus("CREATED")
        .productId(orderRequest.getProductId())
        .orderDate(Instant.now())
        .quantity(orderRequest.getQuantity())
        .build();
        orderRepository.save(order);
        log.info("Calling Payment Service to complete the payment");
    
        

PaymentRequest paymentRequest=PaymentRequest.builder()
.orderId(order.getId())
.paymentMode(orderRequest.getPaymentMode())
.amount(orderRequest.getTotalAmount())
.build();
String orderStatus=null;

try {
    paymentService.doPayment(paymentRequest);
    log.info("Order has been placed successfully");
    orderStatus="PLACED";
} catch (Exception e) {
    log.info("Error Occured in Payment");
    orderStatus="PAYMENT_FAILED";
}

order.setOrderStatus(orderStatus);
orderRepository.save(order);
        log.info("Order placed successfully with orderId:{}",order.getId());
        return order.getId();
    }

    public OrderResponse getOrderDetails(long orderId) {
      Order order= orderRepository.findById(orderId)
      .orElseThrow(()->new CustomException("Order Not Found", "ORDER_NOT_FOUND",404));


      ProductResponse productResponse=restTemplate.getForObject("productServiceUrl" + order.getProductId(), ProductResponse.class);


      log.info("Getting Payment info from payment Service");

PaymentResponse paymentResponse=restTemplate.getForObject("paymentServiceUrl" + order.getId(), PaymentResponse.class);


      OrderResponse.ProductDetails productDetails=OrderResponse.ProductDetails.builder()
      .productName(productResponse.getProductName())
      .productId(productResponse.getProductId())
      .price(productResponse.getPrice())
      .quantity(productResponse.getQuantity())
      .build();

      OrderResponse.PaymentDetails paymentDetails=OrderResponse.PaymentDetails.builder()
      .paymentId(paymentResponse.getPaymentId())
      .paymentStatus(paymentResponse.getStatus())
      .paymentMode(paymentResponse.getPaymentMode())
      .paymentDate(paymentResponse.getPaymentDate())
      .build();

OrderResponse orderResponse=OrderResponse.builder()
.orderStatus(order.getOrderStatus())
.amount(order.getAmount())
.orderId(order.getId())
.orderDate(order.getOrderDate())
.productDetails(productDetails)
.paymentDetails(paymentDetails)
.build();

return orderResponse;

    }
    
}
