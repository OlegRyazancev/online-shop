package com.ryazancev.admin.repository;

import com.ryazancev.admin.model.OrgRegRequest;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrgRegRequestRepository
        extends JpaRepository<OrgRegRequest, Long> {

    Optional<OrgRegRequest> findByOrganizationId(Long organizationId);

}
