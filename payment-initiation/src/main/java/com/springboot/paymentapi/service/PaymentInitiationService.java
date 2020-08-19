package com.springboot.paymentapi.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.springboot.paymentapi.exception.LimitExceededException;
import com.springboot.paymentapi.model.enums.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.springboot.paymentapi.model.PaymentAcceptedResponse;
import com.springboot.paymentapi.model.PaymentInitiationRequest;

@Component
public class PaymentInitiationService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentInitiationService.class);

    public PaymentAcceptedResponse initiatePayment(final PaymentInitiationRequest request)
    {
        checkAmountLimit(request);

        String paymentId = UUID.randomUUID().toString();
        LOGGER.debug("Payment Id {}", paymentId);

        return new PaymentAcceptedResponse(paymentId, TransactionStatus.Accepted);
    }

    private void checkAmountLimit(final PaymentInitiationRequest request)
    {
        final BigDecimal amount = new BigDecimal(request.getAmount());

        Long digits = Long.parseLong(request.getDebtorIBAN().replaceAll("\\D+", ""));
        Long sumOfDigits = 0L;

        while (digits != 0) {
            sumOfDigits = sumOfDigits + digits % 10;
            digits = digits / 10;
        }
        
        if (BigDecimal.ZERO.compareTo(amount) < 0
                && (sumOfDigits % request.getDebtorIBAN().length()) == 0) {
            LOGGER.error("Limit exceeded error");
            throw new LimitExceededException("Amount limit exceeded");
        }
    }
}
