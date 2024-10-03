package com.example.ecosysweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExitController {

    @RequestMapping(value="/exit")
    public void exitProgramm(){
        System.exit(0);
    }
}
