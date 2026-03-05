package com.contabilis.controller;

import com.contabilis.dto.AuthDTO.*;
import com.contabilis.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // POST /api/auth/cadastro
    @PostMapping("/cadastro")
    public ResponseEntity<AuthResponse> cadastrar(@RequestBody CadastroRequest request) {
        return ResponseEntity.ok(authService.cadastrar(request));
    }
}
