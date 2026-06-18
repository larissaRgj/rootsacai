package com.rootsacai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rootsacai.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

List<Pedido> findByClienteId(Long clienteId);

}