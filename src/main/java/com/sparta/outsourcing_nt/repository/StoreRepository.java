package com.sparta.outsourcing_nt.repository;

import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("SELECT s FROM Store s")
    Slice<Store> findAllStores(Pageable pageable);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.user = :user AND s.status <> 'CLOSED'")
    long countByUser(User user);

    @Query("SELECT s FROM Store s WHERE s.name LIKE %:name%")
    Slice<Store> findStoresByNameContaining(@Param("name") String name, Pageable pageable);
}
