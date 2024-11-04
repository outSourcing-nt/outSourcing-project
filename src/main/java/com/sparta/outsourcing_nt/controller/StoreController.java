package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.dto.store.req.StoreCreateRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import com.sparta.outsourcing_nt.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/store")
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreCreateRequestDto reqDto){
        // @AuthenticationPrincipal UserDetailsImpl userDetails
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(storeService.createStore(reqDto));
    }
}
