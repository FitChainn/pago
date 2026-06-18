package com.fitchain.pago.assembler;

import com.fitchain.pago.controller.PagoController;
import com.fitchain.pago.dto.PagoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<PagoResponseDTO, EntityModel<PagoResponseDTO>> {

    @Override
    public EntityModel<PagoResponseDTO> toModel(PagoResponseDTO pago) {
        EntityModel<PagoResponseDTO> model = EntityModel.of(pago,
                linkTo(methodOn(PagoController.class).obtenerPorId(pago.getId())).withSelfRel(),
                linkTo(methodOn(PagoController.class).obtenerTodos()).withRel("pagos"),
                linkTo(methodOn(PagoController.class).obtenerPorEstado(pago.getEstado())).withRel("pagos-por-estado")
        );

        if (pago.getCliente() != null) {
            model.add(linkTo(methodOn(PagoController.class)
                    .obtenerPorCliente(pago.getCliente().getId()))
                    .withRel("pagos-del-cliente"));
        }

        return model;
    }
}