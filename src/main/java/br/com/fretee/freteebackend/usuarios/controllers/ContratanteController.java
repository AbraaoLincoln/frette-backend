package br.com.fretee.freteebackend.usuarios.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fretee/api/contratante")
public class ContratanteController {
    @GetMapping("/")
    public String sayHello() {
        return "blog";
    }
}
