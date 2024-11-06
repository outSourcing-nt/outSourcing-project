package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.menu.req.MenuRequestDto;
import com.sparta.outsourcing_nt.dto.menu.res.MenuResponseDto;
import com.sparta.outsourcing_nt.dto.menu.res.MenuResponsePage;
import com.sparta.outsourcing_nt.service.MenuService;
import com.sparta.outsourcing_nt.util.result.ApiResult;
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
    public ResponseEntity<ApiResult<MenuResponseDto>> createMenu(@RequestBody MenuRequestDto requestDto, @PathVariable Long storeId, AuthUserDetails authUser){
        return new ResponseEntity<>(
                ApiResult.success("메뉴 생성 완료",
                        menuService.createMenu(requestDto, storeId, authUser)),
                HttpStatus.OK);


    }

    @GetMapping()
    public ResponseEntity<ApiResult<MenuResponsePage>> getAllMenus(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size,
                                                        @RequestParam(required = false, defaultValue = "modifiedAt") String criteria,
                                                        @PathVariable Long storeId){
        return new ResponseEntity<>(
                ApiResult.success("전체 메뉴 조회 성공",
                        menuService.getAllMenus(page, size, criteria, storeId)),
                HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<MenuResponseDto>> updateMenu(@PathVariable Long id, @RequestBody MenuRequestDto requestDto, @PathVariable Long storeId){
        menuService.updateMenu(id, requestDto, storeId);

        return new ResponseEntity<>(
                ApiResult.success(
                        "메뉴 수정 성공",
                        menuService.updateMenu(id, requestDto, storeId)),
                HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<MenuResponseDto>> deleteMenu(@PathVariable Long id, @PathVariable Long storeId){
        menuService.deleteMenu(id, storeId);
        return new ResponseEntity<>(
                ApiResult.success(
                        "메뉴 삭제 성공" ,
                        menuService.deleteMenu(id, storeId)),
                HttpStatus.OK);

    }
}
