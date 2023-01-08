package com.unibuc.gymapp.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HttpStatusUtility extends ResponseEntityExceptionHandler {
    public static ResponseEntity<?> successResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, String>> handleHttpStatusException(Throwable e) {
        e.printStackTrace();

        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
