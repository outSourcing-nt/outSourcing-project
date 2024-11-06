package com.sparta.outsourcing_nt.repository;

import com.sparta.outsourcing_nt.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("SELECT s FROM Store s")
    Slice<Store> findAllStores(Pageable pageable);
}
