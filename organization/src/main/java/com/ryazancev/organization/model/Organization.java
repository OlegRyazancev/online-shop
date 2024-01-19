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
            name = "organization_id_sequence",
            sequenceName = "organization_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "organization_id_sequence"
    )
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description", unique = true)
    private String description;

    //todo: add logo (logo-service)

}
