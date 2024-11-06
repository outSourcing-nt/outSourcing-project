package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.store.req.StoreCreateRequestDto;
import com.sparta.outsourcing_nt.dto.store.req.StoreModifyRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreListResponseDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.exception.ApplicationException;
import com.sparta.outsourcing_nt.exception.ErrorCode;
import com.sparta.outsourcing_nt.repository.StoreRepository;
import com.sparta.outsourcing_nt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponseDto createStore(StoreCreateRequestDto reqDto, AuthUserDetails authUser) {
        User user = userRepository.findByEmail(authUser.getUser().getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.INVALID_FORMAT)); // 로그인 된 유저 정보가 없음

        Store store = reqDto.toEntity();
        store.setUser(user);

        storeRepository.save(store);

        return store.toResponseDto();
    }

    @Transactional
    public StoreResponseDto modifyStore(Long storeId, StoreModifyRequestDto reqDto, AuthUserDetails authUser) {
        User user = userRepository.findByEmail(authUser.getUser().getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.INVALID_FORMAT)); // 로그인 된 유저 정보가 없음

        Store store = findStoreById(storeId);

        // 가게 소유자의 id와 일치하는지 확인
        if (!store.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_FORMAT);
        }

        store.modifyData(reqDto);

        return store.toResponseDto();
    }

    public StoreListResponseDto getAllStores(Pageable pageable) {
        Slice<Store> slice = storeRepository.findAllStores(pageable);

        if (slice.isEmpty() && pageable.getPageNumber() > 0) {
            return new StoreListResponseDto(
                    0, pageable.getPageNumber(), false, false, false, false, null
            );
        }

        return new StoreListResponseDto(slice);
    }

    public StoreResponseDto getStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ApplicationException(ErrorCode.INVALID_FORMAT)
        );

        return store.toResponseDto();
    }


    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(
                () -> new ApplicationException(ErrorCode.INVALID_FORMAT)
        );
    }
}
