package com.fitchain.pago.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PagoResponseDTO {

    private Long id;
    private BigDecimal montoPagar;
    private String metodoPago;
    private LocalDate fechaPago;
    private String estado;

    private ClienteDTO cliente;
}
