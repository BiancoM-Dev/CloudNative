package com.pcfactory.ventas.consumidor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaProductoMensaje {

    private String nombreCliente;
    private Long idCliente;
    private Long idProducto;
    private String nombreProducto;
    private BigDecimal precio;
    private Integer cantidad;
}
