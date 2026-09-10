package com.rootsacai.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rootsacai.model.Pedido;
import com.rootsacai.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido criar(Pedido pedido) {
        if (pedido.getTotal() == null || pedido.getTotal() <= 0) {
            throw new IllegalArgumentException("Total do pedido deve ser maior que zero");
        }
        pedido.setStatus("aguardando");
        pedido.setCriadoEm(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarStatus(Long id, String novoStatus) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            String[] statusValidos = {"aguardando", "em_preparo", "pronto", "em_rota", "entregue", "cancelado"};
            
            boolean statusValido = false;
            for (String status : statusValidos) {
                if (status.equals(novoStatus)) {
                    statusValido = true;
                    break;
                }
            }
            
            if (!statusValido) {
                throw new IllegalArgumentException("Status inválido: " + novoStatus);
            }
            
            pedido.setStatus(novoStatus);
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }
}
