package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.dto.menu.req.MenuRequestDto;
import com.sparta.outsourcing_nt.dto.menu.res.MenuResponseDto;
import com.sparta.outsourcing_nt.dto.menu.res.MenuResponsePage;
import com.sparta.outsourcing_nt.entity.Menu;
import com.sparta.outsourcing_nt.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto requestDto) {
        Menu menu = new Menu(requestDto);
        Menu saveMenu = menuRepository.save(menu);
        return new MenuResponseDto(saveMenu);
    }

    public MenuResponsePage getAllMenus(int page, int size, String criteria) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, criteria));
        Page<Menu> menus = menuRepository.findAll(pageable);
        return new MenuResponsePage(menus);
    }

    @Transactional
    public void updateMenu(Long id, MenuRequestDto requestDto){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu not found with id: " + id));
        menu.updateData(requestDto);
    }

    @Transactional
    public void deleteMenu(Long id){
        menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu not found with id: " + id));
        menuRepository.deleteById(id);
    }
}
