package com.ryazancev.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user_credentials")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCredential implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Transient
    private String passwordConfirmation;

    @CollectionTable(name = "users_roles")
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @Column(name = "customer_id")
    private Long customerId;
}
