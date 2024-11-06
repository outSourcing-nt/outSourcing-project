package com.sparta.outsourcing_nt.dto.store.res;

import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.entity.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StoreListResponseDto {
    private long elementsCount;
    private long currentSliceNumber;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;
    private List<StoreResponseDto> storeList;


    public StoreListResponseDto(Slice<Store> slice) {
        this.elementsCount = slice.getNumberOfElements();
        this.currentSliceNumber = slice.getNumber();
        this.hasNext = slice.hasNext();
        this.hasPrevious = slice.hasPrevious();
        this.isFirst = slice.isFirst();
        this.isLast = slice.isLast();
        this.storeList = slice.getContent()
                .stream()
                .filter(store -> store.getStatus() != StoreStatus.CLOSED)
                .map(Store::toResponseDto).toList();
    }
}
