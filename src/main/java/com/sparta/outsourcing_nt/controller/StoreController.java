package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {
    private final StoreService storeService;
}
