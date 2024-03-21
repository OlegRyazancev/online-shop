package com.ryazancev.logo.util.exception;

import com.ryazancev.logo.util.exception.custom.LogoUploadException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static LogoUploadException getLogoUpload(){

        return new LogoUploadException();
    }
}
