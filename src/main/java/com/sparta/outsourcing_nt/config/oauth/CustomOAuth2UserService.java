package com.sparta.outsourcing_nt.config.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.exception.ApplicationException;
import com.sparta.outsourcing_nt.exception.ErrorCode;
import com.sparta.outsourcing_nt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    public CustomOAuth2UserService(ObjectMapper objectMapper, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        return super.loadUser(userRequest);
    }

    @Transactional
    public User addNaverIdToUser(NaverUserInfo userInfo, AuthUserDetails authUser) {
        // 기존에 네이버 아이디가 존재 하는지
        Optional<User> userOp = userRepository.findByNaverProviderId(userInfo.getId());
        if(userOp.isPresent()) {
            throw new ApplicationException(ErrorCode.INVALID_FORMAT);// 기존에 존재 하는 네이버 아이디 오류
        }
        if(authUser.getUser().getNaverProviderId() != null){
            throw new ApplicationException(ErrorCode.INVALID_FORMAT); // 이미 아이디에 네이버 아이디가 등록이 되어있음
        }
        authUser.getUser().setNaverProviderId(userInfo.getId());
        return userRepository.save(authUser.getUser()); // 등록 및 반환
    }

    @Transactional
    public User naverLogin(NaverUserInfo userInfo) {
        Optional<User> userOp = userRepository.findByNaverProviderId(userInfo.getId());

        if(userOp.isEmpty()) {
            throw new ApplicationException(ErrorCode.INVALID_FORMAT);
        }
        return userOp.get();
    }


    public String getAccessToken(String code, String state) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .queryParam("state", state)
                .toUriString();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("Failed to obtain access token");
        }
    }

    public NaverUserInfo getUserInfo(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode responseNode = jsonNode.get("response");
            return objectMapper.treeToValue(responseNode, NaverUserInfo.class);
        } else {
            throw new RuntimeException("Failed to fetch user info");
        }
    }
}