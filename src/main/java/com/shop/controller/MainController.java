package com.shop.controller;


import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private  final ItemService itemService;
    @GetMapping(value ="/")
    public String main(ItemSearchDto itemSearchDto, Model model){
        model.addAttribute("itemSearchDto",itemSearchDto);
        return "main";
    }
    @GetMapping(value = "/main/list")
    public ResponseEntity<List<MainItemDto>> main(ItemSearchDto itemSearchDto, @RequestParam("offset") int offset
            , @RequestParam("limit") int limit){
        System.out.println("aaaaa");
        List<MainItemDto> items = itemService.getMainItemPage(itemSearchDto,offset,limit);
        return new ResponseEntity<>(items, HttpStatus.OK);

    }
    @GetMapping("/recommend")
    public String recommend(Model model) {

        return "recommend"; // map.html 템플릿 뷰를 반환
    }

}
