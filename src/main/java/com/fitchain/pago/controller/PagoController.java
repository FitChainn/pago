package com.fitchain.pago.controller;

import com.fitchain.pago.assembler.PagoModelAssembler;
import com.fitchain.pago.dto.PagoRequestDTO;
import com.fitchain.pago.dto.PagoResponseDTO;
import com.fitchain.pago.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Tag(name = "PAGOS", description = "GESTIÓN DE PAGOS")
@RestController
@RequestMapping("/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;
    @Autowired
    private PagoModelAssembler assembler;

    @Operation(summary = "CREAR PAGO", description = "Crea un nuevo pago. Acceso: ADMIN, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "503", description = "Microservicio no disponible")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @PostMapping
    public ResponseEntity<EntityModel<PagoResponseDTO>> crear(@Valid @RequestBody PagoRequestDTO requestDTO) {
        log.info("POST /v1/pagos - CREAR PAGO clienteId={}", requestDTO.getClienteId());
        PagoResponseDTO creado = pagoService.crear(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(creado));
    }

    @Operation(summary = "OBTENER TODOS LOS PAGOS", description = "Retorna la lista de todos los pagos. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDTO>>> obtenerTodos() {
        log.info("GET /v1/pagos - LISTAR TODOS");
        List<EntityModel<PagoResponseDTO>> pagos = pagoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(pagos,
                linkTo(methodOn(PagoController.class).obtenerTodos()).withSelfRel()));
    }

    @Operation(summary = "OBTENER PAGO POR ID", description = "Retorna un pago específico por su ID. Acceso: ADMIN, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /v1/pagos/{} - BUSCAR POR ID", id);
        return ResponseEntity.ok(assembler.toModel(pagoService.obtenerPorId(id)));
    }

    @Operation(summary = "OBTENER PAGOS POR CLIENTE", description = "Retorna todos los pagos de un cliente. Acceso: ADMIN, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDTO>>> obtenerPorCliente(@PathVariable Long clienteId) {
        log.info("GET /v1/pagos/cliente/{} - BUSCAR POR CLIENTE", clienteId);
        List<EntityModel<PagoResponseDTO>> pagos = pagoService.obtenerPorCliente(clienteId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(pagos,
                linkTo(methodOn(PagoController.class).obtenerPorCliente(clienteId)).withSelfRel()));
    }

    @Operation(summary = "OBTENER PAGOS POR ESTADO", description = "Retorna pagos filtrados por estado (PENDIENTE, COMPLETADO, FALLIDO). Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDTO>>> obtenerPorEstado(@PathVariable String estado) {
        log.info("GET /v1/pagos/estado/{} - BUSCAR POR ESTADO", estado);
        List<EntityModel<PagoResponseDTO>> pagos = pagoService.obtenerPorEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(pagos,
                linkTo(methodOn(PagoController.class).obtenerPorEstado(estado)).withSelfRel()));
    }

    @Operation(summary = "ACTUALIZAR PAGO", description = "Actualiza un pago existente. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PagoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequestDTO requestDTO) {
        log.info("PUT /v1/pagos/{} - ACTUALIZAR PAGO", id);
        return ResponseEntity.ok(assembler.toModel(pagoService.actualizar(id, requestDTO)));
    }

    @Operation(summary = "ELIMINAR PAGO", description = "Elimina un pago por su ID. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /v1/pagos/{} - ELIMINAR PAGO", id);
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
