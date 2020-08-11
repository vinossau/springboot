package com.springboot.paymentApi.model.enums;

public enum TransactionStatus {
    PAYMENT_ACCEPTED ("Accepted"),
    PAYMENT_REJECTED("Rejected");


    private String status;

    private TransactionStatus(String status) {
    	this.status = status;
    }
    
    public String getStatus() {
    	return status;
    }
    
}
