package com.sparta.outsourcing_nt.config.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.outsourcing_nt.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
    public ResponseEntity<?> naverCallback(@RequestParam String code, @RequestParam String state) throws JsonProcessingException {
        log.info("--------------------naverCallback---------------------------");
        System.out.println("Received code: " + code);
        System.out.println("Received state: " + state);



        // 여기에서 코드를 사용하여 액세스 토큰을 요청하는 로직을 추가합니다.
        String accessToken = customOAuth2UserService.getAccessToken(code,state); // 액세스 토큰 요청 메서드 호출
        log.info("에세스 토큰 요청 : {}",accessToken);


        // 사용자 정보를 요청
        CustomOAuth2UserService.NaverUserInfo userInfo = customOAuth2UserService.getUserInfo(accessToken); // 사용자 정보 요청 메서드 호출
        log.info("--------------------naverCallback---------------------------");
        log.info("userInfo : {}",userInfo.getName());
        log.info("--------------------naverCallback---------------------------");

        // 인증이 성공적으로 완료된 후에 JWT를 발급하거나 필요한 처리를 수행합니다.
//        String token = jwtUtil.generateToken(userInfo); // 사용자 정보를 기반으로 JWT 생성

        return ResponseEntity.ok("Callback successful"); // 이 부분을 적절한 응답으로 변경
    }
}
