package com.rootsacai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rootsacai.model.Produto;
import com.rootsacai.repository.ProdutoRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obter(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        }
        return ResponseEntity.status(404).body(Map.of("erro", "Produto não encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ProdutoRequest request) {
        try {
            Produto produto = new Produto();
            produto.setNome(request.getNome());
            produto.setPreco(request.getPreco());
            Produto salvo = produtoRepository.save(produto);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ProdutoRequest request) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isPresent()) {
            Produto p = produto.get();
            p.setNome(request.getNome());
            p.setPreco(request.getPreco());
            Produto atualizado = produtoRepository.save(p);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.status(404).body(Map.of("erro", "Produto não encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("msg", "Produto deletado com sucesso"));
        }
        return ResponseEntity.status(404).body(Map.of("erro", "Produto não encontrado"));
    }

    public static class ProdutoRequest {
        public String nome;
        public double preco;

        public String getNome() { return nome; }
        public double getPreco() { return preco; }
    }
}
