package com.sparta.outsourcing_nt.dto.auth.req;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserLoginReqDto {
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*]).{8,}$",
            message = "비밀번호는 최소 8자 이상, 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
    @Column(nullable = false)
    private String password;
}