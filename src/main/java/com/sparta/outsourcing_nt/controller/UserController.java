package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.dto.user.req.UserSignUpRequestDto;
import com.sparta.outsourcing_nt.entity.UserRole;
import com.sparta.outsourcing_nt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<String> userJoin(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        userService.registerUser(userSignUpRequestDto, UserRole.ROLE_USER);
        return ResponseEntity.ok("사용자 가입 성공");
    }

    @PostMapping("/owners")
    public ResponseEntity<String> ownerJoin(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        userService.registerUser(userSignUpRequestDto, UserRole.ROLE_OWNER);
        return ResponseEntity.ok("사장님 가입 성공");
    }
}
