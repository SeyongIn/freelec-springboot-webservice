package com.insd.study.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ReactController {
    @GetMapping("/react")
    public String postsSave() {
        return "react.html";
    }
}
