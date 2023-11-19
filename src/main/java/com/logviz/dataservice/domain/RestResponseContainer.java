package com.logViz.dataservice.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponseContainer<T> implements Serializable {
    private boolean ok;
    private T data;
    private ResponseError error;

    public static <D> ResponseEntity<RestResponseContainer<D>> getSuccessResponse(D responseBody) {
        return ResponseEntity.ok(RestResponseContainer.<D>builder()
                .ok(true)
                .data(responseBody)
                .build());
    }

    public static ResponseEntity<RestResponseContainer<?>> getErrorResponse(String errorMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>(RestResponseContainer.builder()
                .ok(false)
                .error(new ResponseError(errorMessage))
                .build(), httpStatus);
    }
}

