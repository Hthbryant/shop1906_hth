package com.qf.controller;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/info")
public class ErrorController {

    @RequestMapping("/error")
    public String error(String msg, ModelMap map){
        map.put("msg",msg);
        return "showError";
    }

}
