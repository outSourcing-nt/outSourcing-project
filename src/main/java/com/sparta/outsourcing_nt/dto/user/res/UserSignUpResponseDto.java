package com.sparta.outsourcing_nt.dto.user.res;

import com.sparta.outsourcing_nt.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserSignUpResponseDto {
    private long id;

    private String name;

    private String email;

    private String phone;

    private LocalDateTime createdAt;

    public UserSignUpResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt();
    }
}
