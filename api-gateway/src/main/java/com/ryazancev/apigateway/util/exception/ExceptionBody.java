package com.ryazancev.apigateway.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ExceptionBody {

    private String message;
    private HttpStatus httpStatus;
}
