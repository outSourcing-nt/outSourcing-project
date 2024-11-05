package com.sparta.outsourcing_nt.dto.user.res;

import com.sparta.outsourcing_nt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserListResponseDto {
    long elementsCount;
    long currentPage;
    long nextPage;
    boolean hasNextPage;
    long pageSize;
    List<UserInfoResponseDto> userList;

    public UserListResponseDto(Page<User> userPage) {
        elementsCount = userPage.getTotalElements();
        currentPage = userPage.getNumber();
        nextPage = userPage.getTotalPages();
        pageSize = userPage.getSize();
        hasNextPage = userPage.hasNext();
        userList = userPage.stream().map(UserInfoResponseDto::new).toList();
    }
}
