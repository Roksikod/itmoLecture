package com.example.itmo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String sayHello () {
        return "Java language!";
    }

    @GetMapping("/printInfo")
    public void printInfo() {
        System.out.println("Hello world!");
    }
}
