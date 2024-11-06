package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.user.req.UserDeleteRequestDto;
import com.sparta.outsourcing_nt.dto.user.req.UserSignUpRequestDto;
import com.sparta.outsourcing_nt.dto.user.res.UserDeleteResponseDto;
import com.sparta.outsourcing_nt.dto.user.res.UserInfoResponseDto;
import com.sparta.outsourcing_nt.dto.user.res.UserListResponseDto;
import com.sparta.outsourcing_nt.dto.user.res.UserSignUpResponseDto;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.entity.UserRole;
import com.sparta.outsourcing_nt.exception.ApplicationException;
import com.sparta.outsourcing_nt.exception.ErrorCode;
import com.sparta.outsourcing_nt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            throw new ApplicationException(ErrorCode.NOT_FOUND_USER);
        }

        // 비밀번호 해싱
        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        // User 객체 생성 및 저장
        User user = userRepository.save(userSignUpRequestDto.toEntity(encodedPassword,userRole));

        return new UserSignUpResponseDto(user);
    }

    @Transactional
    public UserDeleteResponseDto deleteUser(UserDeleteRequestDto userDeleteRequestDto,Long userId, AuthUserDetails authUser) {
        User user = userRepository.findByEmail(authUser.getUser().getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)); // 로그인 된 유저 정보가 없음

        // 유저 아이디 검증
        if(!user.getId().equals(userId) || !authUser.getUser().getId().equals(userId)) {
            throw new ApplicationException(ErrorCode.UNAUTHRIZED_USER);
        }
        // 비밀번호 오류
        if(!user.getPassword().equals(passwordEncoder.encode(userDeleteRequestDto.getPassword()))) {
            new ApplicationException(ErrorCode.UNAUTHRIZED_USER);
        }
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);

        return new UserDeleteResponseDto(user.getId());
    }

    @Transactional
    public UserInfoResponseDto findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );
        return new UserInfoResponseDto(user);
    }
    @Transactional
    public UserInfoResponseDto findCurrentUser(AuthUserDetails authUser) {
        User user = userRepository.findByEmail(authUser.getUser().getEmail()).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );
        return new UserInfoResponseDto(user);
    }

    @Transactional
    public UserListResponseDto findAllUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> userPage = userRepository.findAll(pageable);
        return new UserListResponseDto(userPage);
    }
}
