package com.ryazancev.auth.repository;

import com.ryazancev.auth.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository
        extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    @Modifying
    @Query(value = """
            UPDATE confirmation_tokens ct
            SET ct.confirmed_at = CURRENT_TIMESTAMP
            WHERE ct.token = :token
            """, nativeQuery = true)
    void updateConfirmedAt(@Param("token") String token);
}
