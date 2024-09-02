package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @GetMapping("/map")
    public String map(Model model) {
        // 지도 데이터를 모델에 추가
        model.addAttribute("mapTimestamp", "1720066073538");
        model.addAttribute("mapKey", "2jwqm");

        return "/map"; // map.html 템플릿 뷰를 반환
    }
}