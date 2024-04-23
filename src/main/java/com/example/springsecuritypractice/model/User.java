package com.example.springsecuritypractice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username; // 소셜 로그인의 경우 google_1026578540163
    private String password; // 소셜 로그인의 경우 암호화(겟인데어) 이걸로 로그인할게 아니라 null만 아니면 됨..
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN, ROLE_MANAGER
    private String provider; // google
    private String providerId; // google의 sub (1026578540163)
    @CreationTimestamp
    private Timestamp createDate;

}
