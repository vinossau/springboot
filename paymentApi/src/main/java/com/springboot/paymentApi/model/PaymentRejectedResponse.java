package com.springboot.paymentapi.model;

import com.springboot.paymentapi.model.enums.ErrorReasonCode;
import com.springboot.paymentapi.model.enums.TransactionStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRejectedResponse implements Serializable
{
    private static final long serialVersionUID = 5862192572381039135L;
    @NotNull
    private TransactionStatus status;

    @NotNull
    private String reason;

    @NotNull
    private ErrorReasonCode reasonCode;

   /* public PaymentRejectedResponse(TransactionStatus rejected, String message, ErrorReasonCode unknownCertificate) {
        status = rejected;
        reason = message;
        reasonCode = unknownCertificate;
    }*/
}

