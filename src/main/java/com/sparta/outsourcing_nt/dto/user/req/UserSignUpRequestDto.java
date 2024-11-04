package com.sparta.outsourcing_nt.dto.user.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String address;
    private String status; // 예: "ACTIVE" 또는 "INACTIVE"
}