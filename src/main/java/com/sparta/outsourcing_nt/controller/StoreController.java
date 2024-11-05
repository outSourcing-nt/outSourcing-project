package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.store.req.StoreModifyRequestDto;
import com.sparta.outsourcing_nt.dto.store.req.StoreCreateRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreListResponseDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import com.sparta.outsourcing_nt.service.StoreService;
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
    public ResponseEntity<StoreResponseDto> createStore(
            @RequestBody @Valid StoreCreateRequestDto reqDto) {
        // @AuthenticationPrincipal UserDetailsImpl userDetails
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(storeService.createStore(reqDto));
    }

    @PutMapping("/store/{storeId}")
    public ResponseEntity<StoreResponseDto> modifyStore(
            @PathVariable Long storeId,
            @RequestBody StoreModifyRequestDto reqDto,
            @AuthenticationPrincipal AuthUserDetails authUser
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.modifyStore(storeId, reqDto, authUser));
    }

    @GetMapping("/store")
    public ResponseEntity<StoreListResponseDto> getAllStores(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC ) Pageable pageable
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.getAllStores(pageable));
    }
}
