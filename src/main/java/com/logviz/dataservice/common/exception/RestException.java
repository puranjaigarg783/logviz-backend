package com.logViz.dataservice.common.exception;

public class RestException extends RuntimeException{
    public RestException(String s, Throwable err) {
        super(s, err);
    }
}
