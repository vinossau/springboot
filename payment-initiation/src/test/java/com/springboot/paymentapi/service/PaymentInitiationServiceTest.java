package com.springboot.paymentapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import com.springboot.paymentapi.exception.LimitExceededException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springboot.paymentapi.model.PaymentAcceptedResponse;
import com.springboot.paymentapi.model.PaymentInitiationRequest;
import com.springboot.paymentapi.model.enums.TransactionStatus;


@ExtendWith(MockitoExtension.class)
public class PaymentInitiationServiceTest
{


    private PaymentInitiationService subject;

    @BeforeEach
    public void setUp()
    {
        subject = new PaymentInitiationService();
    }


    @Test
    public void testInitiatePayment_With_Valid_Request()
    {

        final PaymentInitiationRequest request = new PaymentInitiationRequest("NL91ABNA0417164301",
                "NL91ABNA0417164302", "500.00", "EUR", "U1000");

        PaymentAcceptedResponse actual = subject.initiatePayment(request);

        assertEquals(TransactionStatus.Accepted, actual.getStatus());
        assertNotNull(actual.getPaymentId());

    }

    @Test
    public void testInitiatePayment_Expected_Reject_Payment_With_Limit_Exceeded_Exception()
    {
        final PaymentInitiationRequest request = new PaymentInitiationRequest("NL91ABNA0417164300",
                "NL91ABNA0417164301", "500.00", "EUR", "U1000");

        Exception exception = Assertions.assertThrows(LimitExceededException.class,
                () -> subject.initiatePayment(request));

        assertTrue(exception.getMessage().contains("Amount limit exceeded"));
    }


    @Test
    public void testInitiatePayment_With_Amount_Less_Than_Zero_Expected_Accept_The_Payment()
    {
        final PaymentInitiationRequest request = new PaymentInitiationRequest("NL91ABNA0417164304",
                "NL91ABNA0417164308", "-100.00", "EUR", "U1005");

         PaymentAcceptedResponse actual = subject.initiatePayment(request);

        assertEquals(TransactionStatus.Accepted, actual.getStatus());
        assertNotNull(actual.getPaymentId());
    }
}
