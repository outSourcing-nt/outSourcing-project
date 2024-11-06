package com.sparta.outsourcing_nt.repository;

import com.sparta.outsourcing_nt.entity.Review;
import com.sparta.outsourcing_nt.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByStoreAndRatingBetweenOrderByCreatedAtDesc(Store store, int minRating, int maxRating, Pageable pageable);
}
