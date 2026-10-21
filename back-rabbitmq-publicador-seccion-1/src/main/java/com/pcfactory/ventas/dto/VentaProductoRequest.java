package com.pcfactory.ventas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaProductoRequest {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 150, message = "El nombre del cliente admite hasta 150 caracteres")
    private String nombreCliente;

    @NotNull(message = "El id del cliente es obligatorio")
    @Positive(message = "El id del cliente debe ser mayor que cero")
    private Long idCliente;

    @NotNull(message = "El id del producto es obligatorio")
    @Positive(message = "El id del producto debe ser mayor que cero")
    private Long idProducto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre del producto admite hasta 150 caracteres")
    private String nombreProducto;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    @Digits(
            integer = 13,
            fraction = 2,
            message = "El precio admite hasta 13 enteros y 2 decimales"
    )
    private BigDecimal precio;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    private Integer cantidad;
}
