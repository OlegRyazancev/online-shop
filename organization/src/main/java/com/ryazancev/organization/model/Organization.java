package com.ryazancev.organization.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description", unique = true)
    private String description;

    @Column(name = "logo")
    private String logo;

    @Column(name = "owner_id")
    private Long ownerId;
}
