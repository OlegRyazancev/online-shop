package com.ryazancev.product.util.exception;

import com.ryazancev.config.ServiceStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionBody {
    private String message;

    private Map<String, String> errors;

    private ServiceStage serviceStage;

    private HttpStatus httpStatus;

    public ExceptionBody(String message) {
        this.message = message;
    }

    public ExceptionBody(String message,
                         ServiceStage serviceStage,
                         HttpStatus httpStatus) {
        this.message = message;
        this.serviceStage = serviceStage;
        this.httpStatus = httpStatus;
    }
}
