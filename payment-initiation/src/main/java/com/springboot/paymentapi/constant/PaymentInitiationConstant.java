
package com.springboot.paymentapi.constant;


public class PaymentInitiationConstant
{
    private PaymentInitiationConstant() {
    }

    public static final String X_REQUEST_ID_PARAM = "X-Request-Id";
    public static final String SIGNATURE_PARAM = "Signature";
    public static final String SIGNATURE_CERTIFICATE_PARAM = "Signature-Certificate";
    public static final String IBAN_REGEX = "[A-Z]{2}[0-9]{2}[a-zA-Z0-9]{1,30}";
    public static final String AMOUNT_REGEX = "-?[0-9]+(\\.[0-9]{1,3})?";
    public static final String CURRENCY_REGEX = "[A-Z]{3}";
    public static final String ALGORITHM_SHA256WITHRSA = "SHA256WithRSA";
    public static final String ALGORITHM_SHA256 = "SHA-256";
    public static final String CONTENT_TYPE = "application/json";

}
