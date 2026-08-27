package com.rootsacai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rootsacai.model.Pedido;
import com.rootsacai.repository.PedidoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obter(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        }
        return ResponseEntity.status(404).body(Map.of("erro", "Pedido não encontrado"));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoRepository.findByClienteId(clienteId));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PedidoRequest request) {
        try {
            Pedido pedido = new Pedido();
            pedido.setTamanho(request.getTamanho());
            pedido.setTotal(request.getTotal());
            pedido.setClienteId(request.getClienteId());
            pedido.setClienteNome(request.getClienteNome());
            pedido.setClienteTel(request.getClienteTel());
            pedido.setStatus("aguardando");
            pedido.setCriadoEm(LocalDateTime.now());
            
            Pedido salvo = pedidoRepository.save(pedido);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("erro", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) {
            Pedido p = pedido.get();
            p.setStatus(status);
            Pedido atualizado = pedidoRepository.save(p);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.status(404).body(Map.of("erro", "Pedido não encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("msg", "Pedido deletado com sucesso"));
        }
        return ResponseEntity.status(404).body(Map.of("erro", "Pedido não encontrado"));
    }

    public static class PedidoRequest {
        public String tamanho;
        public double total;
        public Long clienteId;
        public String clienteNome;
        public String clienteTel;

        public String getTamanho() { return tamanho; }
        public double getTotal() { return total; }
        public Long getClienteId() { return clienteId; }
        public String getClienteNome() { return clienteNome; }
        public String getClienteTel() { return clienteTel; }
    }
}
