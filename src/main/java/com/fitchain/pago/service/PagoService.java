package com.fitchain.pago.service;

import com.fitchain.pago.WebClient.ClienteClient;
import com.fitchain.pago.dto.ClienteDTO;
import com.fitchain.pago.dto.PagoRequestDTO;
import com.fitchain.pago.dto.PagoResponseDTO;
import com.fitchain.pago.model.Pago;
import com.fitchain.pago.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ClienteClient clienteClient;

    private PagoResponseDTO toResponseDTO(Pago pago, ClienteDTO cliente) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(pago.getId());
        dto.setMontoPagar(pago.getMontoPagar());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setFechaPago(pago.getFechaPago());
        dto.setEstado(pago.getEstado());
        dto.setCliente(cliente);
        return dto;
    }

    public PagoResponseDTO crear(PagoRequestDTO requestDTO) {
        log.info("CREANDO PAGO PARA clienteId={}", requestDTO.getClienteId());

        ClienteDTO cliente = clienteClient.obtenerClientePorId(requestDTO.getClienteId());

        Pago pago = new Pago();
        pago.setClienteId(requestDTO.getClienteId());
        pago.setMontoPagar(requestDTO.getMontoPagar());
        pago.setMetodoPago(requestDTO.getMetodoPago());
        pago.setFechaPago(requestDTO.getFechaPago());
        pago.setEstado("PENDIENTE");

        Pago guardado = pagoRepository.save(pago);
        log.info("PAGO CREADO CON ID: {}", guardado.getId());
        return toResponseDTO(guardado, cliente);
    }

    public List<PagoResponseDTO> obtenerTodos() {
        log.info("OBTENIENDO TODOS LOS PAGOS");
        return pagoRepository.findAll().stream()
                .map(p -> toResponseDTO(p, clienteClient.obtenerClientePorId(p.getClienteId())))
                .toList();
    }

    public PagoResponseDTO obtenerPorId(Long id) {
        log.info("BUSCANDO PAGO CON ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pago con id " + id + " no encontrado"));
        return toResponseDTO(pago, clienteClient.obtenerClientePorId(pago.getClienteId()));
    }

    public List<PagoResponseDTO> obtenerPorCliente(Long clienteId) {
        log.info("BUSCANDO PAGOS DEL CLIENTE {}", clienteId);
        ClienteDTO cliente = clienteClient.obtenerClientePorId(clienteId);
        return pagoRepository.findByClienteId(clienteId).stream()
                .map(p -> toResponseDTO(p, cliente))
                .toList();
    }

    public List<PagoResponseDTO> obtenerPorEstado(String estado) {
        log.info("BUSCANDO PAGOS CON ESTADO {}", estado);
        return pagoRepository.findByEstado(estado).stream()
                .map(p -> toResponseDTO(p, clienteClient.obtenerClientePorId(p.getClienteId())))
                .toList();
    }

    public PagoResponseDTO actualizar(Long id, PagoRequestDTO requestDTO) {
        log.info("ACTUALIZANDO PAGO CON ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pago con id " + id + " no encontrado"));

        ClienteDTO cliente = clienteClient.obtenerClientePorId(requestDTO.getClienteId());

        pago.setClienteId(requestDTO.getClienteId());
        pago.setMontoPagar(requestDTO.getMontoPagar());
        pago.setMetodoPago(requestDTO.getMetodoPago());
        pago.setFechaPago(requestDTO.getFechaPago());

        Pago actualizado = pagoRepository.save(pago);
        log.info("PAGO CON ID {} ACTUALIZADO CORRECTAMENTE", id);
        return toResponseDTO(actualizado, cliente);
    }

    public void eliminar(Long id) {
        log.info("ELIMINANDO PAGO CON ID: {}", id);
        if (!pagoRepository.existsById(id)) {
            throw new NoSuchElementException("Pago con id " + id + " no encontrado");
        }
        pagoRepository.deleteById(id);
        log.info("PAGO CON ID {} ELIMINADO EXITOSAMENTE", id);
    }
}
