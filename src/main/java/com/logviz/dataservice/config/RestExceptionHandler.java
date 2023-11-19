package com.logViz.dataservice.config;

import com.logViz.dataservice.domain.ResponseError;
import com.logViz.dataservice.domain.RestResponseContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${com.logViz.rest.exception.message.500}")
    private String INTERNAL_SERVER_EXCEPTION_MESSAGE;

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<RestResponseContainer<?>> handleUnknownError (Exception ex, HttpServletRequest request) {
        log.error("Unknown Exception Occurred, request path: {}", request.getRequestURI(), ex);
        RestResponseContainer<?> resp = RestResponseContainer.builder()
                .error(new ResponseError(INTERNAL_SERVER_EXCEPTION_MESSAGE))
                .build();
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<RestResponseContainer<?>> handleValidationException (Exception ex, HttpServletRequest request) {
        log.error("Validation Exception Occurred, request path: {}", request.getRequestURI(), ex);
        RestResponseContainer<?> resp = RestResponseContainer.builder()
                .error(new ResponseError(ex.getMessage()))
                .build();
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalAccessException.class})
    protected ResponseEntity<RestResponseContainer<?>> handleUnAuthorisedAccessException (Exception ex, HttpServletRequest request) {
        log.error("IllegalAccessException Occurred, request path: {}", request.getRequestURI(), ex);
        RestResponseContainer<?> resp = RestResponseContainer.builder()
                .error(new ResponseError(ex.getMessage()))
                .build();
        return new ResponseEntity<>(resp, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({SQLException.class, DuplicateKeyException.class, BadSqlGrammarException.class, DataIntegrityViolationException.class})
    public ResponseEntity<?> handleSQLExceptions(Exception ex, HttpServletRequest request) {
        log.error("SQL Occurred, request path: {}", request.getRequestURI(), ex);
        String message = NestedExceptionUtils.getMostSpecificCause(ex).getMessage();
        RestResponseContainer<?> resp = RestResponseContainer.builder()
                .error(new ResponseError(message))
                .build();
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Validation Exception Occurred, request path: {}", request.getContextPath(), ex);
        ObjectError error = ex.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
        ResponseEntity resp = RestResponseContainer.getErrorResponse(
                Objects.isNull(error) ? "Invalid Request" : error.getDefaultMessage()
                , HttpStatus.BAD_REQUEST);
        return resp;
    }
}
