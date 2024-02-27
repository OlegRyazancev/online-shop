package com.ryazancev.common.exception;

import com.ryazancev.common.config.ServiceStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

