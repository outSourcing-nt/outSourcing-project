package com.sparta.outsourcing_nt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role; // 예: ROLE_USER, ROLE_ADMIN 등

    @Column(nullable = false)
    private String address;

}
