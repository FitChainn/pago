package com.fitchain.pago.repository;

import com.fitchain.pago.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByClienteId(Long clienteId);

    List<Pago> findByEstado(String estado);

    List<Pago> findByClienteIdAndEstado(Long clienteId, String estado); //no se usaa
}
