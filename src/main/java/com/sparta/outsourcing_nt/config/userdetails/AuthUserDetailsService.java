package com.sparta.outsourcing_nt.config.userdetails;

import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.exception.ApplicationException;
import com.sparta.outsourcing_nt.exception.ErrorCode;
import com.sparta.outsourcing_nt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // 사용자 정보를 가져올 Repository

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        if (user == null) {
            throw new ApplicationException(ErrorCode.UNAUTHRIZED_USER);
        }
        if (user.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.DELETED_USER);
        }

        return new AuthUserDetails(
                user,
                user.getRole().getAuthorities() // UserRole에서 권한을 가져오는 메소드
        );
    }
}