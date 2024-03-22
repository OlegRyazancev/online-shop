package com.ryazancev.admin.util.exception;

import com.ryazancev.common.config.ServiceStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Oleg Ryazancev
 */

@Data
@AllArgsConstructor
public class ExceptionBody {

    private String message;

    private Map<String, String> errors;

    private ServiceStage serviceStage;

    private HttpStatus httpStatus;

    private String code;

    private LocalDateTime timestamp;

    public ExceptionBody(final String message) {
        this.message = message;
    }

    public ExceptionBody(final String message,
                         final ServiceStage serviceStage,
                         final HttpStatus httpStatus,
                         final String code,
                         final LocalDateTime timestamp) {
        this.message = message;
        this.serviceStage = serviceStage;
        this.httpStatus = httpStatus;
        this.code = code;
        this.timestamp = timestamp;
    }

    public ExceptionBody(final String message,
                         final ServiceStage serviceStage,
                         final HttpStatus httpStatus) {
        this.message = message;
        this.serviceStage = serviceStage;
        this.httpStatus = httpStatus;
        this.code = CustomErrorCode.OS_ADMIN_INTERNAL_500.name();
        this.timestamp = LocalDateTime.now();
    }
}
