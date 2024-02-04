package com.ryazancev.admin.repository;

import com.ryazancev.admin.model.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRequestRepository
        extends JpaRepository<RegistrationRequest,Long> {

}
