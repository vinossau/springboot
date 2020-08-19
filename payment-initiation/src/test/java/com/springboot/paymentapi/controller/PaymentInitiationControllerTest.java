package com.springboot.paymentapi.controller;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.paymentapi.model.PaymentAcceptedResponse;
import com.springboot.paymentapi.model.PaymentInitiationRequest;
import com.springboot.paymentapi.service.PaymentInitiationService;
import com.springboot.paymentapi.model.enums.TransactionStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test for PaymentInitiationController
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentInitiationControllerTest
{

    @MockBean
    private PaymentInitiationService mockPaymentInitiationService;

    @Autowired(required = true)
    private MockMvc mockMvc;

    @Autowired(required = true)
    private ObjectMapper objectMapper;

    private PaymentInitiationController subject;

    @BeforeEach
    public void setUp()
    {
        subject = new PaymentInitiationController(mockPaymentInitiationService);
    }

    @Test
    public void testInitiatePayment_Expect_Payment_Accepted() throws Exception
    {
        final PaymentInitiationRequest request = new PaymentInitiationRequest("NL91ABNA0417164301",
                "NL91ABNA0417164302", "100.00", "EUR", "U1000");

        final String paymentId = UUID.randomUUID().toString();

        final PaymentAcceptedResponse expected = new PaymentAcceptedResponse(paymentId,
                TransactionStatus.Accepted);
        when(mockPaymentInitiationService.initiatePayment(any())).thenReturn(expected);

        ResponseEntity<PaymentAcceptedResponse> response = subject.initiatePayment(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testInitiatePayment_Payment_Rejected_Invalid_Signature()
            throws Exception
    {
        final PaymentInitiationRequest request = new PaymentInitiationRequest("NL02RABO7134384550",
                "NL94ABNA1008270131", "1.00", null, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/initiate-payment")
                .header("X-Request-Id", "29318e25-cebd-498c-888a-f77672f66449")
                .header("Signature", getValidSignature())
                .header("Signature-Certificate", getValidCertificate())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", is("Rejected")))
                .andExpect(jsonPath("$.reasonCode", is("INVALID_SIGNATURE")));
    }

    @Test
    public void testInitiatePayment_Payment_Rejected_With_Reason_Limit_Exceeded()
            throws Exception
    {
        final PaymentInitiationRequest request = new PaymentInitiationRequest("NL02RABO7134384551",
                "NL94ABNA1008270121", "500.00",null,null);

        mockMvc.perform(MockMvcRequestBuilders.post("/initiate-payment")
                .header("X-Request-Id", "29318e25-cebd-498c-888a-f77672f66449")
                .header("Signature", getValidSignature())
                .header("Signature-Certificate", getValidCertificate())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status", is("Rejected")))
                .andExpect(jsonPath("$.reasonCode", is("INVALID_SIGNATURE")))
                .andExpect(jsonPath("$.reason", containsString("Invalid Signature")));
    }


    
    private String getValidSignature()
    {
        return "AlFr/WbYiekHmbB6XdEO/7ghKd0n6q/bapENAYsL86KoYHqa4eP34xfH9icpQRmTpH0qOkt1vfUPWnaqu+vHBWx/gJXiuVlhayxLZD2w41q8ITkoj4oRLn2U1q8cLbjUtjzFWX9TgiQw1iY0ezpFqyDLPU7+ZzO01JI+yspn2gtto0XUm5KuxUPK24+xHD6R1UZSCSJKXY1QsKQfJ+gjzEjrtGvmASx1SUrpmyzVmf4qLwFB1ViRZmDZFtHIuuUVBBb835dCs2W+d7a+icGOCtGQbFcHvW0FODibnY5qq8v5w/P9i9PSarDaGgYb+1pMSnF3p8FsHAjk3Wccg2a1GQ==";
    }
    
    private String getValidCertificate()
    {
        return "-----BEGIN CERTIFICATE-----MIIDwjCCAqoCCQDxVbCjIKynQjANBgkqhkiG9w0BAQsFADCBojELMAkGA1UEBhMCTkwxEDAOBgNVBAgMB1V0cmVjaHQxEDAOBgNVBAcMB1V0cmVjaHQxETAPBgNVBAoMCFJhYm9iYW5rMRMwEQYDVQQLDApBc3Nlc3NtZW50MSIwIAYDVQQDDBlTYW5kYm94LVRQUDpleGNlbGxlbnQgVFBQMSMwIQYJKoZIhvcNAQkBFhRuby1yZXBseUByYWJvYmFuay5ubDAeFw0yMDAxMzAxMzIyNDlaFw0yMTAxMjkxMzIyNDlaMIGiMQswCQYDVQQGEwJOTDEQMA4GA1UECAwHVXRyZWNodDEQMA4GA1UEBwwHVXRyZWNodDERMA8GA1UECgwIUmFib2JhbmsxEzARBgNVBAsMCkFzc2Vzc21lbnQxIjAgBgNVBAMMGVNhbmRib3gtVFBQOmV4Y2VsbGVudCBUUFAxIzAhBgkqhkiG9w0BCQEWFG5vLXJlcGx5QHJhYm9iYW5rLm5sMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAryLyouTQr1dvMT4qvek0eZsh8g0DQQLlOgBzZwx7iInxYEAgMNxCKXiZCbmWHBYqh6lpPh+BBmrnBQzB+qrSNIyd4bFhfUlQ+htK08yyL9g4nyLt0LeKuxoaVWpInrB5FRzoEY5PPpcEXSObgr+pM71AvyJtQLxZbqTao4S7TRKecUm32Wwg+FWY/StSKlox3QmEaxEGU7aPkaQfQs4hrtuUePwKrbkQ2hQdMpvI5oXRWzTqafvEQvND+IyLvZRqf0TSvIwsgtJd2tch2kqPoUwng3AmUFleJbMjFNzrWM7TH9LkKPItYtSuMTzeSe9o0SmXZFgcEBh5DnETZqIVuQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQASFOkJiKQuL7fSErH6y5Uwj9WmmQLFnit85tjbo20jsqseTuZqLdpwBObiHxnBz7o3M73PJAXdoXkwiMVykZrlUSEy7+FsNZ4iFppoFapHDbfBgM2WMV7VS6NK17e0zXcTGySSRzXsxw0yEQGaOU8RJ3Rry0HWo9M/JmYFrdBPP/3sWAt/+O4th5Jyk8RajN3fHFCAoUz4rXxhUZkf/9u3Q038rRBvqaA+6c0uW58XqF/QyUxuTD4er9veCniUhwIX4XBsDNxIW/rwBRAxOUkG4V+XqrBb75lCyea1o/9HIaq1iIKI4Day0piMOgwPEg1wF383yd0x8hRW4zxyHcER-----END CERTIFICATE-----";
    }
}