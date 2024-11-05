package com.sparta.outsourcing_nt.repository;

import com.sparta.outsourcing_nt.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
