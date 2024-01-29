package com.ryazancev.auth.repository;

import com.ryazancev.auth.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository
        extends JpaRepository<UserCredential, Long> {

    Optional<UserCredential> findByEmail(String email);
}
