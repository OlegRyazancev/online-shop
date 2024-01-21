package com.ryazancev.config;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnlineShopException extends RuntimeException {

    private String message;
    private Map<String, String> errors;
    private ServiceStage serviceStage;
    private HttpStatus httpStatus;


    public OnlineShopException(String message) {
        super(message);
        this.message = message;
    }

}

