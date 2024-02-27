package com.ryazancev.auth.service;

import com.ryazancev.auth.model.ConfirmationToken;

/**
 * @author Oleg Ryazancev
 */

public interface ConfirmationTokenService {
    String confirm(String token);

    void save(ConfirmationToken token);
}
