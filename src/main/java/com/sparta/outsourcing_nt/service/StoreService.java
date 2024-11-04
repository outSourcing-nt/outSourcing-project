package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.dto.store.req.StoreCreateRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponseDto createStore(StoreCreateRequestDto reqDto) {
        Store store = storeRepository.save(Store.from(reqDto));
        return store.to();
    }
}
