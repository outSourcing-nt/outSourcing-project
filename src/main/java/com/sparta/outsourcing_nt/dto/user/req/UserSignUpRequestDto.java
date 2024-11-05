package com.sparta.outsourcing_nt.dto.user.req;

import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*]).{8,}$",
            message = "비밀번호는 최소 8자 이상, 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String password;

    private String name;

    private String phone;

    private String address;

    public User toEntity(UserRole userRole) {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .address(address)
                .role(userRole)
                .build();
    }
}