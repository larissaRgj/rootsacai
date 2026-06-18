package com.rootsacai.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootsacai.model.Usuario;
import com.rootsacai.repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final UsuarioRepository repository;

    public AuthController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/login")
public Map<String, Object> login(@RequestBody Map<String,String> dados) {

System.out.println("Recebido: " + dados);

String email = dados.get("email");
String senha = dados.get("senha");

Usuario usuario = repository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

System.out.println("Banco senha = " + usuario.getSenha());
System.out.println("Digitada = " + senha);

if (!usuario.getSenha().equals(senha)) {
    throw new RuntimeException("Senha incorreta");
}

String perfil = switch (usuario.getPerfilId()) {
    case 1 -> "administrador";
    case 2 -> "cliente";
    case 3 -> "preparador";
    case 4 -> "motoboy";
    default -> "cliente";
};

return Map.of(
    "id", usuario.getId(),
    "nome", usuario.getNome(),
    "email", usuario.getEmail(),
    "perfil", perfil,
    "token", "token-demo"
);

}
}