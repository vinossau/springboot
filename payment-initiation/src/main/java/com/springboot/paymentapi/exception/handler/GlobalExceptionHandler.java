
package com.springboot.paymentapi.exception.handler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.paymentapi.exception.InvalidSignatureException;
import com.springboot.paymentapi.exception.LimitExceededException;
import com.springboot.paymentapi.exception.UnknownCertificateException;
import com.springboot.paymentapi.model.PaymentRejectedResponse;
import com.springboot.paymentapi.model.enums.ErrorReasonCode;
import com.springboot.paymentapi.model.enums.TransactionStatus;

@ControllerAdvice
public class GlobalExceptionHandler
{

    private PaymentRejectedResponse response;

    @ExceptionHandler(value = UnknownCertificateException.class)
    public ResponseEntity<PaymentRejectedResponse> handleUnknownCertificateException(
            final UnknownCertificateException exception)
    {
        response = new PaymentRejectedResponse(TransactionStatus.Rejected, exception.getMessage(),
                ErrorReasonCode.UNKNOWN_CERTIFICATE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<PaymentRejectedResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception)
    {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error ->
        {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        response = new PaymentRejectedResponse(TransactionStatus.Rejected, errors.toString(),
                ErrorReasonCode.INVALID_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidSignatureException.class)
    public ResponseEntity<PaymentRejectedResponse> handleInvalidSignatureException(
            final InvalidSignatureException exception)
    {
        response = new PaymentRejectedResponse(TransactionStatus.Rejected, exception.getMessage(),
                ErrorReasonCode.INVALID_SIGNATURE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<PaymentRejectedResponse> handleGenericException(final Exception exception)
    {
        response = new PaymentRejectedResponse(TransactionStatus.Rejected, exception.getMessage(),
                ErrorReasonCode.GENERAL_ERROR);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = LimitExceededException.class)
    public ResponseEntity<PaymentRejectedResponse> handleLimitExceededException(
            final LimitExceededException exception)
    {
        response = new PaymentRejectedResponse(TransactionStatus.Rejected, exception.getMessage(),
                ErrorReasonCode.LIMIT_EXCEEDED);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<PaymentRejectedResponse> handleAccessDeniedException(
            AccessDeniedException exception)
    {
        response = new PaymentRejectedResponse(TransactionStatus.Rejected, exception.getMessage(),
                ErrorReasonCode.UNKNOWN_CERTIFICATE);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
