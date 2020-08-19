package com.springboot.paymentapi.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.springboot.paymentapi.model.enums.ErrorReasonCode;
import com.springboot.paymentapi.model.enums.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PaymentRejectedResponse implements Serializable
{

    private static final long serialVersionUID = -1564589722771178922L;
    @NotNull
    private TransactionStatus status;

    @NotNull
    private String reason;

    @NotNull
    private ErrorReasonCode reasonCode;
}
