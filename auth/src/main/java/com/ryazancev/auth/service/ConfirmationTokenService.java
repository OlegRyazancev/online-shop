package com.ryazancev.auth.service;

import com.ryazancev.auth.model.ConfirmationToken;

public interface ConfirmationTokenService {
    String confirm(String token);

    void saveConfirmationToken(ConfirmationToken token);
}
