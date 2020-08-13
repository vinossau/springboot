package com.springboot.paymentapi.controller;


import com.springboot.paymentapi.model.PaymentAcceptedResponse;
import com.springboot.paymentapi.model.PaymentInitiationRequest;
import com.springboot.paymentapi.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
public class PaymentApiController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentApiController.class);

    @Autowired
    PaymentService paymentService;

    @PostMapping(path = "/payment-initiate-request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentAcceptedResponse> initiatePayment(
            @Valid @RequestBody final PaymentInitiationRequest request)
    {
        logger.info("Payment controller---");
        PaymentAcceptedResponse response = paymentService.initiatePayment(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
