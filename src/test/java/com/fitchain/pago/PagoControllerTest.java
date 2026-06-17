package com.fitchain.pago;

import com.fitchain.pago.config.SecurityConfig;
import com.fitchain.pago.controller.PagoController;
import com.fitchain.pago.dto.PagoRequestDTO;
import com.fitchain.pago.dto.PagoResponseDTO;
import com.fitchain.pago.filter.RolHeaderFilter;
import com.fitchain.pago.service.PagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagoController.class)
@Import({SecurityConfig.class, RolHeaderFilter.class})
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PagoResponseDTO pResponse;
    private PagoRequestDTO pRequest;

    @BeforeEach
    void setUp() {
        pResponse = new PagoResponseDTO();
        pResponse.setId(1L);
        pResponse.setMontoPagar(new BigDecimal("50000"));
        pResponse.setMetodoPago("DEBITO");
        pResponse.setFechaPago(LocalDate.of(2025, 1, 1));
        pResponse.setEstado("COMPLETADO");

        pRequest = new PagoRequestDTO();
        pRequest.setClienteId(1L);
        pRequest.setMontoPagar(new BigDecimal("50000"));
        pRequest.setMetodoPago("DEBITO");
        pRequest.setFechaPago(LocalDate.of(2025, 1, 1));
    }

    @Test
    void Post_crear201() throws Exception {
        when(pagoService.crear(any(PagoRequestDTO.class))).thenReturn(pResponse);

        mockMvc.perform(post("/v1/pagos")
                        .header("X-User-Rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void Get_obtenerTodos() throws Exception {
        when(pagoService.obtenerTodos()).thenReturn(List.of(pResponse));

        mockMvc.perform(get("/v1/pagos")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void Get_obtenerPorId() throws Exception {
        when(pagoService.obtenerPorId(1L)).thenReturn(pResponse);

        mockMvc.perform(get("/v1/pagos/1")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void Put_actualizar() throws Exception {
        when(pagoService.actualizar(eq(1L), any(PagoRequestDTO.class))).thenReturn(pResponse);

        mockMvc.perform(put("/v1/pagos/1")
                        .header("X-User-Rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void Delete_eliminar() throws Exception {
        mockMvc.perform(delete("/v1/pagos/1")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isNoContent());
    }
}
