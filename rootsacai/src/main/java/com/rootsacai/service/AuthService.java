package com.rootsacai.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rootsacai.model.Usuario;
import com.rootsacai.repository.UsuarioRepository;
import com.rootsacai.util.JwtUtil;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> login(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        if (!usuario.getAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }
        
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }
        
        String token = jwtUtil.gerarToken(usuario.getId(), usuario.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("nome", usuario.getNome());
        response.put("email", usuario.getEmail());
        response.put("perfil", usuario.getPerfilId());
        response.put("id", usuario.getId());
        
        return response;
    }

    public Map<String, Object> registrar(String nome, String email, String senha, Integer perfilId) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfilId(perfilId != null ? perfilId : 2); // 2 = Cliente
        usuario.setAtivo(true);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        String token = jwtUtil.gerarToken(usuarioSalvo.getId(), usuarioSalvo.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("nome", usuarioSalvo.getNome());
        response.put("email", usuarioSalvo.getEmail());
        response.put("perfil", usuarioSalvo.getPerfilId());
        response.put("id", usuarioSalvo.getId());
        
        return response;
    }
}
