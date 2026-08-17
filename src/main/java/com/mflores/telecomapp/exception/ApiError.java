package com.mflores.telecomapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiError {

    private String error;
    private Integer status;
    private LocalDateTime dateTime;
    private Map<String, String> errors;


    public ApiError(String error, Integer status, LocalDateTime dateTime) {
        this.error = error;
        this.status = status;
        this.dateTime = dateTime;
    }
}
