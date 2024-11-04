package com.sparta.outsourcing_nt.dto.menu.res;

import com.sparta.outsourcing_nt.entity.Menu;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MenuResponseDto {
    private Long id;
    private String category;
    private String name;
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MenuResponseDto(Menu menu) {
        this.id = menu.getId();
        this.category = menu.getCategory();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.createdAt = menu.getCreatedAt();
        this.modifiedAt = menu.getModifiedAt();
    }
}
