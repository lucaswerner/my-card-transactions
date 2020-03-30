package com.mycard.transactions.handler;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
                errorAttributes.remove("exception");
                errorAttributes.remove("trace");
                return errorAttributes;
            }
        };
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
    }

    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException(IllegalStateException e, HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public void handleHystrixRuntimeException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.GATEWAY_TIMEOUT.value(), "Request timeout");
    }
}
