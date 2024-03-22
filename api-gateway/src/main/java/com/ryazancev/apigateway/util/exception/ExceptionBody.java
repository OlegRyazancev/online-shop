package com.ryazancev.apigateway.util.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Data
@Builder
@AllArgsConstructor
public class ExceptionBody {

    private String message;
    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;
}
