package com.ryazancev.auth.repository;

import com.ryazancev.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCustomerId(Long customerId);

    @Modifying
    @Query(value = """
            UPDATE users u
            SET u.confirmed = TRUE
            WHERE u.email = :email
            """, nativeQuery = true)
    void enableUser(@Param("email") String email);
}
