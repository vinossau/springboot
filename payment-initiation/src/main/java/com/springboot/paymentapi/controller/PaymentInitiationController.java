package com.springboot.paymentapi.controller;

import javax.validation.Valid;

import com.springboot.paymentapi.model.PaymentAcceptedResponse;
import com.springboot.paymentapi.model.PaymentInitiationRequest;
import com.springboot.paymentapi.service.PaymentInitiationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class PaymentInitiationController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentInitiationController.class);

    @Autowired
    private PaymentInitiationService paymentInitiationService;

    @PostMapping(path = "/initiate-payment", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<PaymentAcceptedResponse> initiatePayment(
           @Valid @RequestBody final PaymentInitiationRequest request)
    {
        LOGGER.info("Payment Initiate controller.......");
        PaymentAcceptedResponse response = paymentInitiationService.initiatePayment(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
