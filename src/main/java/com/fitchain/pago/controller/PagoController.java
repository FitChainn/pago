package com.fitchain.pago.controller;

import com.fitchain.pago.dto.PagoRequestDTO;
import com.fitchain.pago.dto.PagoResponseDTO;
import com.fitchain.pago.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @PostMapping
    public ResponseEntity<PagoResponseDTO> crear(@Valid @RequestBody PagoRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crear(requestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pagoService.obtenerPorCliente(clienteId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.obtenerPorEstado(estado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequestDTO requestDTO) {
        return ResponseEntity.ok(pagoService.actualizar(id, requestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
