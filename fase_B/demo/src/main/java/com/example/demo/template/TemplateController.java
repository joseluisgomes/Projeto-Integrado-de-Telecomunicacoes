package com.example.demo.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class TemplateController {

    @GetMapping(path = "index")
    public String getIndex() { return "index"; }

    @GetMapping(path = "data")
    public String getData() { return "data"; }
}
