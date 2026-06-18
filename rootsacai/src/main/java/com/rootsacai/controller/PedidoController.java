package com.rootsacai.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootsacai.model.Pedido;
import com.rootsacai.repository.PedidoRepository;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin("*")
public class PedidoController {

private final PedidoRepository repository;

public PedidoController(PedidoRepository repository) {
    this.repository = repository;
}

@GetMapping
public List<Pedido> listar() {
    return repository.findAll();
}

@PostMapping
public Pedido salvar(@RequestBody Pedido pedido) {

    if (pedido.getStatus() == null) {
        pedido.setStatus("aguardando");
    }

    return repository.save(pedido);
}

@PatchMapping("/{id}/status")
public Pedido atualizarStatus(
        @PathVariable Long id,
        @RequestParam String status) {

    Pedido pedido = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

    pedido.setStatus(status);

    return repository.save(pedido);
}

}