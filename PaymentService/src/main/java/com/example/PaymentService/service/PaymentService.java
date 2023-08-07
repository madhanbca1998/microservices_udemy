package com.example.PaymentService.service;


import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PaymentService.entity.TransactionDetails;
import com.example.PaymentService.model.PaymentMode;
import com.example.PaymentService.model.PaymentMode;
import com.example.PaymentService.model.PaymentRequest;
import com.example.PaymentService.model.PaymentResponse;
import com.example.PaymentService.repository.TransactionDetailsRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PaymentService {

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;


    public long doPayment(PaymentRequest paymentRequest){
log.info("Recording Payment Details:{}",paymentRequest);

TransactionDetails transactionDetails=TransactionDetails.builder()
.paymentDate(Instant.now())
.paymentMode(paymentRequest.getPaymentMode().name())
.paymentStatus("Success")
.orderId(paymentRequest.getOrderId())
.amount(paymentRequest.getAmount())
.build();

transactionDetailsRepository.save(transactionDetails);
log.info("Transaction has been completed with Id:{}",transactionDetails);
return transactionDetails.getId();
    }


    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {
      TransactionDetails transactionDetails=  transactionDetailsRepository.findByOrderId(orderId);

PaymentResponse paymentResponse=PaymentResponse.builder()
.paymentId(transactionDetails.getId())
.paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
.paymentDate(transactionDetails.getPaymentDate())
.status(transactionDetails.getPaymentStatus())
.amount(transactionDetails.getAmount())
.orderId(transactionDetails.getOrderId())
.build();

return paymentResponse;
    };
    
}
