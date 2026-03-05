package com.contabilis.dto;

import lombok.Data;

public class AuthDTO {

    @Data
    public static class LoginRequest {
        private String email;
        private String senha;
    }

    @Data
    public static class CadastroRequest {
        private String nome;
        private String email;
        private String senha;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String nome;
        private String email;
        private String role;

        public AuthResponse(String token, String nome, String email, String role) {
            this.token = token;
            this.nome = nome;
            this.email = email;
            this.role = role;
        }
    }
}
