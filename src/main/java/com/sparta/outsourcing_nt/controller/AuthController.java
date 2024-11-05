package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.dto.auth.req.UserLoginReqDto;
import com.sparta.outsourcing_nt.exception.ApplicationException;
import com.sparta.outsourcing_nt.exception.ErrorCode;
import com.sparta.outsourcing_nt.jwt.JwtUtil;
import com.sparta.outsourcing_nt.util.result.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<String>> login(@Valid @RequestBody UserLoginReqDto userLoginReqDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginReqDto.getEmail(), userLoginReqDto.getPassword())
            );
            String token = jwtUtil.generateToken(authentication); // JWT 토큰 생성

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer "+ token);

            return new ResponseEntity<>(
                    ApiResult.success(
                            "로그인 성공"),
                    headers,
                    HttpStatus.OK);

        } catch (AuthenticationException e) {
            throw new ApplicationException(ErrorCode.INVALID_FORMAT);
        }
    }

}
