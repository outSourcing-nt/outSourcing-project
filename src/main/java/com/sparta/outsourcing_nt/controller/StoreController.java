package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.store.req.StoreModifyRequestDto;
import com.sparta.outsourcing_nt.dto.store.req.StoreCreateRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreDeleteDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreListResponseDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import com.sparta.outsourcing_nt.service.StoreService;
import com.sparta.outsourcing_nt.util.result.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/store")
    public ResponseEntity<ApiResult<StoreResponseDto>> createStore(
            @RequestBody @Valid StoreCreateRequestDto reqDto,
            @AuthenticationPrincipal AuthUserDetails authUser
    ) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "가게 생성 성공",
                        storeService.createStore(reqDto, authUser)
                ),
                HttpStatus.CREATED);
    }

    @PutMapping("/store/{storeId}")
    public ResponseEntity<ApiResult<StoreResponseDto>> modifyStore(
            @PathVariable Long storeId,
            @RequestBody StoreModifyRequestDto reqDto,
            @AuthenticationPrincipal AuthUserDetails authUser
    ) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "가게 수정 성공",
                        storeService.modifyStore(storeId, reqDto, authUser)
                ),
                HttpStatus.OK);
    }

    @GetMapping("/store")
    public ResponseEntity<ApiResult<StoreListResponseDto>> getAllStores(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "가게 전체 조회 성공",
                        storeService.getAllStores(pageable)
                ),
                HttpStatus.OK);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResult<StoreResponseDto>> getStore(@PathVariable Long storeId) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "가게 단일 조회 성공",
                        storeService.getStore(storeId)
                ),
                HttpStatus.OK);
    }

    @DeleteMapping("/store/{storeId}")
    public ResponseEntity<ApiResult<StoreDeleteDto>> deleteStore(@PathVariable Long storeId, @AuthenticationPrincipal AuthUserDetails authUser) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "가게 폐업 성공",
                        storeService.deleteStore(storeId, authUser)
                ),
                HttpStatus.OK);
    }
}
