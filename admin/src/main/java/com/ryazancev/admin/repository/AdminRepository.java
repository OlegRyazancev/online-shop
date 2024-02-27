package com.ryazancev.admin.repository;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository
        extends JpaRepository<RegistrationRequest, Long> {

    List<RegistrationRequest> findAllByObjectType(ObjectType objectType);
}
