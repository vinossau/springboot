
package com.springboot.paymentapi.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.springboot.paymentapi.constant.PaymentInitiationConstant;

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
@JsonInclude(Include. NON_NULL)
public class PaymentInitiationRequest implements Serializable
{

    private static final long serialVersionUID = 7274980585249473776L;

    public PaymentInitiationRequest(@NotNull(message = "Debtor IBAN is null") @Pattern(regexp = PaymentInitiationConstant.IBAN_REGEX,
            message = "Incorrect Debtor IBAN") String debtorIBAN, @NotNull(message = "Creditor IBAN is null") @Pattern(regexp = PaymentInitiationConstant.IBAN_REGEX,
            message = "Incorrect Creditor IBAN") String creditorIBAN, @NotNull(message = "Amount is null") @Pattern(regexp = PaymentInitiationConstant.AMOUNT_REGEX,
            message = "Invalid Amount") String amount) {
        this.debtorIBAN = debtorIBAN;
        this.creditorIBAN = creditorIBAN;
        this.amount = amount;
    }

    @NotNull(message = "Debtor IBAN is null")
    @Pattern(regexp = PaymentInitiationConstant.IBAN_REGEX, message = "Incorrect Debtor IBAN")
    private String debtorIBAN;

    @NotNull(message = "Creditor IBAN is null")
    @Pattern(regexp = PaymentInitiationConstant.IBAN_REGEX, message = "Incorrect Creditor IBAN")
    private String creditorIBAN;

    @NotNull(message = "Amount is null")
    @Pattern(regexp = PaymentInitiationConstant.AMOUNT_REGEX, message = "Invalid Amount")
    private String amount;

    @Pattern(regexp = PaymentInitiationConstant.CURRENCY_REGEX, message = "Invalid Currency")
    private String currency;

    private String endToEndId;

}
