package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.domain.CurrentUser;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
@Slf4j
public class TestPrivateController {

    @GetMapping("/test")
    public String test() {
        return "Teste de rota privada";
    }

    @GetMapping("/logged-user")
    public String loggedUser(@CurrentUser AuthenticatedUser details) {
        log.info("loggedUser() - details <{}>", details.getUsername());
        return String.valueOf(details.getUsername());
    }
}
