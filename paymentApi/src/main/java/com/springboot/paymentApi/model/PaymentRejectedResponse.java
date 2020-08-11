package com.springboot.paymentApi.model;

import com.springboot.paymentApi.model.enums.ErrorReasonCode;
import com.springboot.paymentApi.model.enums.TransactionStatus;

public class PaymentRejectedResponse {

    private TransactionStatus status;

    private String reason;

    private ErrorReasonCode reasonCode;

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ErrorReasonCode getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(ErrorReasonCode reasonCode) {
        this.reasonCode = reasonCode;
    }
}
