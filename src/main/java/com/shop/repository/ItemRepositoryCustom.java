package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    public List<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, long offset, int limit);

    public List<MainItemDto> getCategoryItemPage(String category, long offset, int limit);

}
