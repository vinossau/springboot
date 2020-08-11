package com.springboot.paymentApi.model;

import com.springboot.paymentApi.model.enums.TransactionStatus;

public class PaymentAcceptedRsponse {

    private String paymentUUID;

    private TransactionStatus status;
}
