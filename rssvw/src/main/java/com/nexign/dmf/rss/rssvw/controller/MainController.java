package com.nexign.dmf.rss.rssvw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rssprice")
public class MainController {
    @GetMapping("index")
    public String index() {
//        return "index";
        return "index";
    }
}

