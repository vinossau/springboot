package com.springboot.paymentApi.controller;

import com.springboot.paymentApi.Exception.GlobalExceptionHandler;
import com.springboot.paymentApi.Exception.InvalidSignatureException;
import com.springboot.paymentApi.digitalsignature.GenerateSignature;
import com.springboot.paymentApi.digitalsignature.VerifySignature;
import com.springboot.paymentApi.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class PaymentApiController {

    @Autowired
    GenerateSignature generalSignature;

    @Autowired
    VerifySignature verifySignature;

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

    @GetMapping(value = "/validate")
    public String validatePaymentApi()
    {
        return "Welcome to Payment API";
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public Customer GetCustomer(@PathVariable Long id) {
        return new Customer(id, "Customer" + id);
    }



    public void ValidateSignature()throws Exception{

        boolean verified = verifySignature.verifySignatureWithPublicKey();
        if(!verified){
            throw new InvalidSignatureException();
        }
    }

}
