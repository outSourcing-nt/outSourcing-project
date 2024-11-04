package com.sparta.outsourcing_nt.repository;

import com.sparta.outsourcing_nt.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
