package com.sparta.outsourcing_nt.dto.menu.res;

import com.sparta.outsourcing_nt.entity.Menu;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MenuResponsePage {
    private List<MenuResponseDto> menus;
    private int totalPages;
    private long totalElements;

    public MenuResponsePage(Page<Menu> page) {
        this.menus = page.getContent().stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }
}
