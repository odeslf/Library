package com.mybooks.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/") // Mapeia para a raiz da aplicação
public class HomeController {

    @GetMapping
    public String home() {
        return "home"; // Retorna o nome do template: src/main/resources/templates/home.html
    }
}