package com.sparta.outsourcing_nt.entity;

import com.sparta.outsourcing_nt.dto.menu.req.MenuRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "menu")
@NoArgsConstructor
//@RequiredArgsConstructor
public class Menu extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> orderMenus; // OrderMenu와의 관계


    public Menu(MenuRequestDto requestDto, Store store) {
        this.category = requestDto.getCategory();
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.store = store;
    }

    public void updateData(MenuRequestDto requestDto){
        this.category = requestDto.getCategory();
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
    }

    public void softDelete() {
        this.setDeletedAt(LocalDateTime.now());
    }
}
