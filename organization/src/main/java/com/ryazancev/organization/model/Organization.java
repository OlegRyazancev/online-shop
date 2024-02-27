package com.ryazancev.organization.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Entity
@Table(name = "organizations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organization implements Serializable {

    @Id
    @SequenceGenerator(
            name = "organizations_id_seq",
            sequenceName = "organizations_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "organizations_id_seq"
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logo")
    private String logo;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
}
