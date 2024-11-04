package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.dto.user.req.UserSignUpRequestDto;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.entity.UserRole;
import com.sparta.outsourcing_nt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserSignUpRequestDto userSignUpRequestDto,UserRole userRole) {
        userRepository.findByEmail(userSignUpRequestDto.getEmail()).orElseThrow(()->
                new RuntimeException("이미 존재하는 아이디입니다."));

        // 비밀번호 해싱
        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        // User 객체 생성
        User user = new User();
        user.setEmail(userSignUpRequestDto.getEmail());
        user.setPassword(encodedPassword);
        user.setName(userSignUpRequestDto.getName());
        user.setPhone(userSignUpRequestDto.getPhone());
        user.setAddress(userSignUpRequestDto.getAddress());
        user.setStatus(userSignUpRequestDto.getStatus());
        user.setRole(userRole); // 기본 역할 설정

        // 사용자 정보 저장
        userRepository.save(user);
    }
}
