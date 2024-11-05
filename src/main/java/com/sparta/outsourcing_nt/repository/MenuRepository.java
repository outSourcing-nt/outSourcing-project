package com.sparta.outsourcing_nt.repository;

import com.sparta.outsourcing_nt.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
