package com.sparta.outsourcing_nt.config.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Slf4j
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
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 네이버에서 받아온 사용자 정보 가져오기
        String providerId = oAuth2User.getAttribute("id");
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        // 저장소에서 사용자 조회
        Optional<User> existingUser = userRepository.findByEmail(email);

        log.info("-------------------------------------------------");
        log.info(providerId);
        log.info(name);
        log.info(email);
        log.info("-------------------------------------------------");

        // 기존 사용자가 없으면 새로 생성 후 저장
        // if (existingUser.isEmpty()) {
        //     User newUser = new User();
        //     newUser.setProviderId(providerId);
        //     newUser.setName(name);
        //     newUser.setEmail(email);
        //     userRepository.save(newUser);
        // }

        return oAuth2User;
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

        log.info("Generated URL for access token: {}", url);

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

    public static class NaverUserInfo {
        private String email;
        private String name;

        // Getter, Setter 추가
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}