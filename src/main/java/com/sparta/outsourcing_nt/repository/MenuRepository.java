package com.sparta.outsourcing_nt.repository;

import com.sparta.outsourcing_nt.entity.Menu;
import com.sparta.outsourcing_nt.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Page<Menu> findByStoreAndDeletedAtIsNull(Store store, Pageable pageable);
}
