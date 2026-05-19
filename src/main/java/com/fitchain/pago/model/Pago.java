package com.fitchain.pago.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagar;

    @Column(nullable = false)
    private String metodoPago;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Column(nullable = false)
    private String estado;
}
