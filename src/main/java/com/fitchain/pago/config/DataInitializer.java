package com.fitchain.pago.config;

import com.fitchain.pago.model.Pago;
import com.fitchain.pago.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PagoRepository pagoRepository;

    @Override
    public void run(String... args) {
        if (pagoRepository.count() == 0) {
            log.info("Cargando datos de prueba para Pago...");

            LocalDate hoy = LocalDate.now();

            Pago p1 = new Pago();
            p1.setClienteId(1L);
            p1.setMontoPagar(new BigDecimal("25000"));
            p1.setMetodoPago("EFECTIVO");
            p1.setFechaPago(hoy);
            p1.setEstado("PAGADO");

            Pago p2 = new Pago();
            p2.setClienteId(2L);
            p2.setMontoPagar(new BigDecimal("60000"));
            p2.setMetodoPago("TARJETA");
            p2.setFechaPago(hoy.minusDays(5));
            p2.setEstado("PAGADO");

            Pago p3 = new Pago();
            p3.setClienteId(3L);
            p3.setMontoPagar(new BigDecimal("180000"));
            p3.setMetodoPago("TRANSFERENCIA");
            p3.setFechaPago(hoy.minusDays(2));
            p3.setEstado("PENDIENTE");

            Pago p4 = new Pago();
            p4.setClienteId(1L);
            p4.setMontoPagar(new BigDecimal("25000"));
            p4.setMetodoPago("EFECTIVO");
            p4.setFechaPago(hoy.minusMonths(1));
            p4.setEstado("CANCELADO");

            pagoRepository.saveAll(List.of(p1, p2, p3, p4));
            log.info("Datos de prueba cargados: {} pagos", pagoRepository.count());
        } else {
            log.info("Ya existen datos en la base de datos, omitiendo inicialización");
        }
    }
}
