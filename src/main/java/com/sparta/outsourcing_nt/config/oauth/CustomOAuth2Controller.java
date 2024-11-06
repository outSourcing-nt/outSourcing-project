package com.sparta.outsourcing_nt.config.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.jwt.JwtUtil;
import com.sparta.outsourcing_nt.util.result.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CustomOAuth2Controller {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login/oauth2/code/naver")
    public ResponseEntity<?> naverCallback(@RequestParam String code, @RequestParam String state, @AuthenticationPrincipal AuthUserDetails authUser) throws JsonProcessingException {

        String accessToken = customOAuth2UserService.getAccessToken(code,state); // 액세스 토큰 요청 메서드 호출

        NaverUserInfo userInfo = customOAuth2UserService.getUserInfo(accessToken); // 사용자 정보 요청 메서드 호출

        User user;
        if(authUser != null){
            user = customOAuth2UserService.addNaverIdToUser(userInfo,authUser);
        }else{
            user = customOAuth2UserService.naverLogin(userInfo);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getRole().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(authentication); // JWT 토큰 생성

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ token);

        return new ResponseEntity<>(
                ApiResult.success(
                        "Bearer "+ token),
                headers,
                HttpStatus.OK);
    }
}
