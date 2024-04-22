package com.example.springsecuritypractice.repository;

import com.example.springsecuritypractice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository
// @Repository 어노테이션이 없어도 IOC가 됨, JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer> {

}
