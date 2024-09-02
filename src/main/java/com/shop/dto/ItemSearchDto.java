package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType; // 조회 날짜

    private ItemSellStatus searchSellStatus; //상태

    private String searchBy; // 조회 유형

    private String searchQuery =""; // 검색 단어
}
