package com.demo.demandfarm.error;

import com.demo.demandfarm.dto.error.ErrorResponse;
import com.demo.demandfarm.dto.error.ErrorWrapper;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        log.error("MethodArgumentNotValidException occurred", ex);
        final List<String> message =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();

        final List<ErrorResponse> errorResponse = new ArrayList<>();
        message.forEach(
                msg ->
                        errorResponse.add(
                                new ErrorResponse(
                                        9001,
                                        msg)));

        return new ResponseEntity<>(new ErrorWrapper(errorResponse), headers, status);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorWrapper> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.error("ConstraintViolationException occurred", ex);
        final List<ErrorResponse> errorResponse = new ArrayList<>();
        ex.getConstraintViolations()
                .forEach(
                        violation ->
                                errorResponse.add(
                                        new ErrorResponse(
                                                9002,
                                                violation.getMessage())));
        return ResponseEntity.badRequest().body(new ErrorWrapper(errorResponse));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorWrapper> handleBusinessException(
            final BusinessException ex) {
        log.error("Business exception occurred", ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getErrorResponseWrapper());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorWrapper> handleFileSizexception(MaxUploadSizeExceededException ex){
        return ResponseEntity.badRequest().body(new ErrorWrapper(
                List.of(
                        new ErrorResponse(9003,"Please make sure the file size is less than 10MB")
                )
        )
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorWrapper> handleBusinessException(
            final ExceptionHandler ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorWrapper(
                        List.of(
                                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"It's not you, It's us. We are working on it")
                        )
                )
        );
    }
}
