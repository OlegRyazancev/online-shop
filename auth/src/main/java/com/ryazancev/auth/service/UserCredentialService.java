package com.ryazancev.auth.service;

import com.ryazancev.auth.dto.UserCredentialDTO;
import com.ryazancev.auth.model.UserCredential;

public interface UserCredentialService {

    UserCredentialDTO create(UserCredentialDTO userCredentialDTO);

    UserCredential getByEmail(String username);
}
