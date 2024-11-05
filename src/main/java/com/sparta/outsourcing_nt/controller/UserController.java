package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.user.req.UserDeleteRequestDto;
import com.sparta.outsourcing_nt.dto.user.req.UserSignUpRequestDto;
import com.sparta.outsourcing_nt.dto.user.res.UserDeleteResponseDto;
import com.sparta.outsourcing_nt.dto.user.res.UserInfoResponseDto;
import com.sparta.outsourcing_nt.dto.user.res.UserListResponseDto;
import com.sparta.outsourcing_nt.dto.user.res.UserSignUpResponseDto;
import com.sparta.outsourcing_nt.entity.UserRole;
import com.sparta.outsourcing_nt.service.UserService;
import com.sparta.outsourcing_nt.util.result.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResult<UserSignUpResponseDto>> userJoin(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "회원가입 성공",
                        userService.registerUser(userSignUpRequestDto, UserRole.ROLE_USER)),
                HttpStatus.OK);
    }

    @PostMapping("/owners")
    public ResponseEntity<ApiResult<UserSignUpResponseDto>> ownerJoin(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "회원가입 성공",
                        userService.registerUser(userSignUpRequestDto, UserRole.ROLE_OWNER)),
                HttpStatus.OK);

    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResult<UserDeleteResponseDto>> deleteUser(@RequestBody UserDeleteRequestDto userDeleteRequestDto,@PathVariable Long userId ,@AuthenticationPrincipal AuthUserDetails authUser) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "회원삭제 성공",
                        userService.deleteUser(userDeleteRequestDto,userId, authUser)),
                HttpStatus.OK);

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResult<UserInfoResponseDto>> findUser(@PathVariable Long userId) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "회원조회 성공",
                        userService.findUser(userId)),
                HttpStatus.OK);

    }
    @GetMapping("/current-user")
    public ResponseEntity<ApiResult<UserInfoResponseDto>> findCurrentUser(@AuthenticationPrincipal AuthUserDetails authUser) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "회원조회 성공",
                        userService.findCurrentUser(authUser)),
                HttpStatus.OK);

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public ResponseEntity<ApiResult<UserListResponseDto>> findAllUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                                                       @RequestParam(required = false, defaultValue = "10") int size) {
        return new ResponseEntity<>(
                ApiResult.success(
                        "회원조회 성공",
                        userService.findAllUsers(page,size)),
                HttpStatus.OK);

    }

}
