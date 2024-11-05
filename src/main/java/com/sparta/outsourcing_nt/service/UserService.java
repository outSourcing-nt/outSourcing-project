package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.user.req.UserDeleteRequestDto;
import com.sparta.outsourcing_nt.dto.user.req.UserSignUpRequestDto;
import com.sparta.outsourcing_nt.dto.user.res.UserDeleteResponseDto;
import com.sparta.outsourcing_nt.dto.user.res.UserSignUpResponseDto;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.entity.UserRole;
import com.sparta.outsourcing_nt.exception.ApplicationException;
import com.sparta.outsourcing_nt.exception.ErrorCode;
import com.sparta.outsourcing_nt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserSignUpResponseDto registerUser(UserSignUpRequestDto userSignUpRequestDto, UserRole userRole) {

        if(userRepository.findByEmail(userSignUpRequestDto.getEmail()).isPresent()) {
            throw new ApplicationException(ErrorCode.INVALID_FORMAT);
        }

        // 비밀번호 해싱
        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        // User 객체 생성 및 저장
        User user = userRepository.save(userSignUpRequestDto.toEntity(encodedPassword,userRole));

        return new UserSignUpResponseDto(user);
    }

    @Transactional
    public UserDeleteResponseDto deleteUser(UserDeleteRequestDto userDeleteRequestDto, AuthUserDetails authUser) {
        log.info("유저검색전");
        User user = userRepository.findByEmail(authUser.getUser().getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.INVALID_FORMAT)); // 로그인 된 유저 정보가 없음

        log.info("유저검색 완료");
        if(!user.getPassword().equals(passwordEncoder.encode(userDeleteRequestDto.getPassword()))) {
            new ApplicationException(ErrorCode.INVALID_FORMAT); // 비밀번호 오류
        }
        log.info("비밀번호 통과");
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);

        return new UserDeleteResponseDto(user.getId());
    }
}
