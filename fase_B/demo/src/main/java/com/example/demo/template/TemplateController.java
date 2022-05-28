package com.example.demo.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class TemplateController {

    @GetMapping(path = "index")
    public String getIndexView() { return "index"; }

    @GetMapping(path = "data")
    public String getDataView() { return "data"; }

    @GetMapping(path = "login")
    public String getLoginView() { return "login"; }
}
