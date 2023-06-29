package com.redbee.msauthmanager.adapter.controller;

import com.redbee.msauthmanager.adapter.controller.model.UserLoginRequest;
import com.redbee.msauthmanager.adapter.controller.model.UserRestaurarPasswordRequest;
import com.redbee.msauthmanager.adapter.controller.model.UserSignInRequest;
import com.redbee.msauthmanager.application.service.AuthManagerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.http.HttpResponse;

@RestController
@Slf4j
@RequestMapping("/api/ms-auth-manager")
@RequiredArgsConstructor
public class AuthManagerControllerAdapter {

    @Autowired
    private AuthManagerService  authManagerService;

    @SneakyThrows
    @PostMapping(value = "/signin")
    public void signin(
            @RequestBody @Valid UserSignInRequest request
    ) {
        log.info("Ejecucion de get '/signin' con request: Email: {}, Username: {}", request.getEmail(), request.getUsername());
        authManagerService.crearUser(request);
        log.info("Ejecucion de get '/signin' finalizada correctamente");
    }

    @SneakyThrows
    @GetMapping(value = "/login")
    public String login(
            @RequestBody @Valid UserLoginRequest request
    ) {
        log.info("Ejecucion de get '/login' con request: {}", request);
        String jwt = authManagerService.loginUser(request);
        log.info("Ejecucion de get '/login' finalizada correctamente");
        return jwt;
    }

    @SneakyThrows
    @PutMapping(value = "/restaurarPassword")
    public void restaurarPassword(
            @RequestBody @Valid UserRestaurarPasswordRequest request
    ) {
        log.info("Ejecucion de get '/restaurarUser' con request: {}", request);
        authManagerService.restaurarContrasenia(request);
        log.info("Ejecucion de get '/restaurarUser' finalizada correctamente");
    }
}
