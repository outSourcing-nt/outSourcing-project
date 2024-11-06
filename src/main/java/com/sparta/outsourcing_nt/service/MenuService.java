package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.menu.req.MenuRequestDto;
import com.sparta.outsourcing_nt.dto.menu.res.MenuResponseDto;
import com.sparta.outsourcing_nt.dto.menu.res.MenuResponsePage;
import com.sparta.outsourcing_nt.entity.Menu;
import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.repository.MenuRepository;
import com.sparta.outsourcing_nt.repository.StoreRepository;
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
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto requestDto, Long storeId, AuthUserDetails authUser) {
        if (authUser.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals("ROLE_OWNER"))) {
            throw new SecurityException("사장님 권한이 있는 사용자만 메뉴를 생성할 수 있습니다.");
        }
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
        Menu menu = new Menu(requestDto, store);
        Menu saveMenu = menuRepository.save(menu);
        return new MenuResponseDto(saveMenu);
    }

    public MenuResponsePage getAllMenus(int page, int size, String criteria, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, criteria));
        Page<Menu> menus = menuRepository.findByStoreAndDeletedAtIsNull(store, pageable);
        return new MenuResponsePage(menus);
    }

    @Transactional
    public MenuResponseDto updateMenu(Long id, MenuRequestDto requestDto, Long storeId, AuthUserDetails authUser) {
        if (authUser.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals("ROLE_OWNER"))) {
            throw new SecurityException("사장님 권한이 있는 사용자만 메뉴를 생성할 수 있습니다.");
        }
        storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu not found with id: " + id));
        menu.updateData(requestDto);

        return new MenuResponseDto(menu);
    }

    @Transactional
    public MenuResponseDto deleteMenu(Long id, Long storeId, AuthUserDetails authUser) {
        if (authUser.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals("ROLE_OWNER"))) {
            throw new SecurityException("사장님 권한이 있는 사용자만 메뉴를 생성할 수 있습니다.");
        }
        storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu not found with id: " + id));
        menu.softDelete();

        return new MenuResponseDto(menu);
    }
}
