package com.rootsacai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rootsacai.model.Usuario;
import com.rootsacai.repository.UsuarioRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(request.getEmail());
            
            if (usuario.isPresent()) {
                Usuario u = usuario.get();
                // TODO: Implementar hash de senha seguro (BCrypt)
                if (u.getSenha().equals(request.getSenha()) && u.getAtivo()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", "token-" + System.currentTimeMillis());
                    response.put("nome", u.getNome());
                    response.put("perfil", u.getPerfilId()); // 1=Admin, 2=Cliente, 3=Preparador, 4=Motoboy
                    response.put("id", u.getId());
                    return ResponseEntity.ok(response);
                }
            }
            
            return ResponseEntity.status(401).body(Map.of("erro", "Credenciais inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }

    public static class LoginRequest {
        public String email;
        public String senha;

        public String getEmail() { return email; }
        public String getSenha() { return senha; }
    }
}
