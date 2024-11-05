package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.dto.user.req.UserSignUpRequestDto;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.entity.UserRole;
import com.sparta.outsourcing_nt.exception.ApplicationException;
import com.sparta.outsourcing_nt.exception.ErrorCode;
import com.sparta.outsourcing_nt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserSignUpRequestDto userSignUpRequestDto,UserRole userRole) {

        if(userRepository.findByEmail(userSignUpRequestDto.getEmail()).isPresent()) {
            throw new ApplicationException(ErrorCode.INVALID_FORMAT);
        }

        // 비밀번호 해싱
        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        // User 객체 생성
        User user = userSignUpRequestDto.toEntity(encodedPassword,userRole);

        // 사용자 정보 저장
        userRepository.save(user);
    }
}
