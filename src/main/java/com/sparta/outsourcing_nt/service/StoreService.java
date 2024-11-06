package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.store.req.StoreCreateRequestDto;
import com.sparta.outsourcing_nt.dto.store.req.StoreModifyRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreDeleteDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreListResponseDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.entity.StoreStatus;
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
                () -> new ApplicationException(ErrorCode.NOT_FOUND_STORE)); // 로그인 된 유저 정보가 없음

        Store store = reqDto.toEntity();
        store.setUser(user);

        storeRepository.save(store);

        return store.toResponseDto();
    }

    @Transactional
    public StoreResponseDto modifyStore(Long storeId, StoreModifyRequestDto reqDto, AuthUserDetails authUser) {
        User user = userRepository.findByEmail(authUser.getUser().getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_STORE)); // 로그인 된 유저 정보가 없음

        Store store = findStoreById(storeId);

        // 가게 소유자의 id와 일치하는지 확인
        if (!store.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.NONE_PERMISSION);
        }

        store.modifyData(reqDto);

        return store.toResponseDto();
    }

    public StoreListResponseDto getAllStores(Pageable pageable) {
        Slice<Store> slice = storeRepository.findAllStores(pageable);

        if (slice.isEmpty() && pageable.getPageNumber() > 0) {
            throw new ApplicationException(ErrorCode.NOT_FOUND);
        }

        return new StoreListResponseDto(slice);
    }

    public StoreResponseDto getStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_STORE)
        );

        if (store.getStatus() == StoreStatus.CLOSED){
            throw new ApplicationException(ErrorCode.CLOSED_STORE);
        }

        return store.toResponseDto();
    }

    @Transactional
    public StoreDeleteDto deleteStore(Long storeId, AuthUserDetails authUser) {
        User user = userRepository.findByEmail(authUser.getUser().getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)); // 로그인 된 유저 정보가 없음

        Store store = findStoreById(storeId);

        // 가게 소유자의 id와 일치하는지 확인
        if (!store.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHRIZED_USER);
        }

        store.close();

        return new StoreDeleteDto(storeId, store.getName());
    }



    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_STORE)
        );
    }
}
