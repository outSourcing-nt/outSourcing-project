package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.dto.menu.req.MenuRequestDto;
import com.sparta.outsourcing_nt.dto.menu.res.MenuResponseDto;
import com.sparta.outsourcing_nt.dto.menu.res.MenuResponsePage;
import com.sparta.outsourcing_nt.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store/{storeId}/menu")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto, @PathVariable Long storeId){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(menuService.createMenu(requestDto, storeId));
    }

    @GetMapping()
    public ResponseEntity<MenuResponsePage> getAllMenus(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size,
                                                        @RequestParam(required = false, defaultValue = "modifiedAt") String criteria,
                                                        @PathVariable Long storeId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(menuService.getAllMenus(page, size, criteria, storeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long id, @RequestBody MenuRequestDto requestDto, @PathVariable Long storeId){
        menuService.updateMenu(id, requestDto, storeId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MenuResponseDto> deleteMenu(@PathVariable Long id, @PathVariable Long storeId){
        menuService.deleteMenu(id, storeId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
