package com.sparta.outsourcing_nt.repository;

import com.sparta.outsourcing_nt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
