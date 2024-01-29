package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.dto.UserCredentialDTO;
import com.ryazancev.auth.model.UserCredential;
import com.ryazancev.auth.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {

    @Override
    public UserCredentialDTO create(UserCredentialDTO userCredentialDTO) {
        return null;
    }

    @Override
    public UserCredential getByEmail(String username) {
        return null;
    }
}
