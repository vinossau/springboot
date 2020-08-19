
package com.springboot.paymentapi.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Scanner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.paymentapi.config.RequestWrapper;
import com.springboot.paymentapi.constant.PaymentInitiationConstant;
import com.springboot.paymentapi.exception.GlobalException;
import com.springboot.paymentapi.exception.InvalidSignatureException;
import com.springboot.paymentapi.exception.UnknownCertificateException;
import com.springboot.paymentapi.model.PaymentRejectedResponse;
import com.springboot.paymentapi.model.enums.ErrorReasonCode;
import com.springboot.paymentapi.model.enums.TransactionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;

public class X509CustomAuthenticationFilter extends X509AuthenticationFilter
{

    private static final Logger LOGGER = LoggerFactory.getLogger(X509CustomAuthenticationFilter.class);

    private final HttpSecurity http;

    public X509CustomAuthenticationFilter(HttpSecurity http)
    {
        this.http = http;
    }

    @Override
    @SuppressWarnings("resource")
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request)
    {
        String signatureInput = request.getHeader(PaymentInitiationConstant.SIGNATURE_PARAM);
        String xRequestId = request.getHeader(PaymentInitiationConstant.X_REQUEST_ID_PARAM);
        String body = "";

        byte[] requestId = xRequestId.getBytes();
        try {
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                Scanner scanner = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
                body = scanner.hasNext() ? scanner.next() : "";

                LOGGER.debug("Request body -> {}", body);
            }

            byte[] payloadMsg = getDigestMessage(body);
            byte[] concatBytes = new byte[requestId.length + payloadMsg.length];
            System.arraycopy(requestId, 0, concatBytes, 0, requestId.length);
            System.arraycopy(payloadMsg, 0, concatBytes, requestId.length, payloadMsg.length);
            Base64.getEncoder().encodeToString(concatBytes);

            setAuthenticationManager(this.http.getSharedObject(AuthenticationManager.class));
            X509Certificate x509Certificate = parseCertificate(request);
            PublicKey publicKey = x509Certificate.getPublicKey();

            byte[] signatureBytes = Base64.getDecoder().decode(signatureInput.getBytes(StandardCharsets.UTF_8));

            Signature signature = Signature.getInstance(PaymentInitiationConstant.ALGORITHM_SHA256WITHRSA);

            signature.initVerify(publicKey);

            signature.update(concatBytes);

            boolean isSignatureOK = signature.verify(signatureBytes);

            LOGGER.info("The signature param {}", isSignatureOK);
            if (!isSignatureOK) {
                throw new InvalidSignatureException("Invalid Signature");
            }

            return x509Certificate;

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException
                | CertificateException exception) {
            LOGGER.error("Error while parsing the signature and certificate", exception);
            throw new GlobalException("Error parsing signature and certificate");
        }
    }

    private X509Certificate parseCertificate(HttpServletRequest request) throws CertificateException
    {
        final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
        final String END_CERT = "-----END CERTIFICATE-----";
        String certStr = request.getHeader(PaymentInitiationConstant.SIGNATURE_CERTIFICATE_PARAM);
        byte[] decoded = Base64.getDecoder().decode(certStr.replaceAll(BEGIN_CERT, "").replaceAll(END_CERT, ""));

        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(decoded));
    }

    private byte[] getDigestMessage(String message) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance(PaymentInitiationConstant.ALGORITHM_SHA256);
        md.update(message.getBytes());
        return md.digest();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        try {
            ServletRequest requestNew = new RequestWrapper(
                    (HttpServletRequest) request);
            HttpServletResponse responseNew = (HttpServletResponse) response;
            getPreAuthenticatedCredentials((HttpServletRequest) requestNew);
            chain.doFilter(requestNew, responseNew);
        } catch (InvalidSignatureException ise) {
            setInvaildSignatureErrorResponse(HttpStatus.BAD_REQUEST,(HttpServletResponse) response, ise);
        }catch(UnknownCertificateException uce){
            setUnknownCertificationResponse(HttpStatus.BAD_REQUEST,(HttpServletResponse) response, uce);
        }catch(Exception e){
            setGenericErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,(HttpServletResponse) response, e);
        }
    }

    private void setInvaildSignatureErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException
    {
        response.setStatus(status.value());
        response.setContentType(PaymentInitiationConstant.CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse(TransactionStatus.Rejected, ex.getLocalizedMessage(),
                ErrorReasonCode.INVALID_SIGNATURE);
        ObjectMapper objectMapper = new ObjectMapper();
        writer.write(objectMapper.writeValueAsString(paymentRejectedResponse));
        writer.close();
    }

    private void setUnknownCertificationResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException
    {
        response.setStatus(status.value());
        response.setContentType(PaymentInitiationConstant.CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse(TransactionStatus.Rejected, ex.getLocalizedMessage(),
                ErrorReasonCode.UNKNOWN_CERTIFICATE);
        ObjectMapper objectMapper = new ObjectMapper();
        writer.write(objectMapper.writeValueAsString(paymentRejectedResponse));
        writer.close();

    }

    private void setGenericErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException
    {
        response.setStatus(status.value());
        response.setContentType(PaymentInitiationConstant.CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse(TransactionStatus.Rejected, ex.getLocalizedMessage(),
                ErrorReasonCode.GENERAL_ERROR);
        ObjectMapper objectMapper = new ObjectMapper();
        writer.write(objectMapper.writeValueAsString(paymentRejectedResponse));
        writer.close();

    }
}