package com.springboot.paymentapi.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
public class PaymentAcceptedResponse implements Serializable
{

    private static final long serialVersionUID = -1969939566239377361L;
    @NotNull
    private String paymentId;

    @NotNull
    private TransactionStatus status;

}
