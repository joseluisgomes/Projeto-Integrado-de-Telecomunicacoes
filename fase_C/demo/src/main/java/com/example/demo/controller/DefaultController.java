package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.example.demo.security.ApplicationUserRole.ADMIN;

@Controller
public class DefaultController {

    @RequestMapping(value = "/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole(ADMIN.name()))
            return "/index";
        return "/data";
    }
}
