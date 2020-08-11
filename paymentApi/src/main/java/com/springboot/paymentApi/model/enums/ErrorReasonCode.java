package com.springboot.paymentApi.model.enums;

public enum ErrorReasonCode {
    UNKNOWN_CERTIFICATE("UNKNOWN_CERTIFICATE"),
    INVALID_SIGNATURE("INVALID_SIGNATURE"),
    INVALID_REQUEST("INVALID_REQUEST"),
    LIMIT_EXCEEDED("LIMIT_EXCEEDED"),
    GENERAL_ERROR("GENERAL_ERROR");


    private String reasonCode;

    private ErrorReasonCode(String reasonCode) {
    	this.reasonCode = reasonCode;
    }
    
    public String getReasonCode() {
    	return reasonCode;
    }
    
}
