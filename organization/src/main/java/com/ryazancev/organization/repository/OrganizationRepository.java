package com.ryazancev.organization.repository;

import com.ryazancev.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByName(String name);

    @Query(value = """
            SELECT EXISTS(SELECT 1
                          FROM organizations
                          WHERE owner_id = :userId
                            AND id = :organizationId)
            """, nativeQuery = true)
    Boolean isOrganizationOwner(@Param("userId") Long userId,
                                @Param("organizationId") Long organizationId);


}
