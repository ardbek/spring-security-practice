package com.example.springsecuritypractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // View를 리턴
public class IndexController {

    @GetMapping({"","/"})
    public String index() {
        //머스테치 기본 폴더 src/main/resources/
        // viewResolver : templates(prefix), .mustache (suffix) 생략가능
        return "index"; // src/main/resources/templates/index.mustache
    }

}
