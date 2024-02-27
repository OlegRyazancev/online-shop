package com.ryazancev.organization.util.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@AllArgsConstructor
@Getter
@Setter
public class DeletedOrganizationException extends RuntimeException {

    private HttpStatus httpStatus;


    public DeletedOrganizationException(
            String message,
            HttpStatus httpStatus) {

        super(message);
        this.httpStatus = httpStatus;
    }
}
