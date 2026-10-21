package com.pcfactory.ventas.consumidor.service;

import com.pcfactory.ventas.consumidor.dto.VentaProductoMensaje;
import com.pcfactory.ventas.consumidor.entity.VentaProducto;
import com.pcfactory.ventas.consumidor.repository.VentaProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaProductoService {

    @Autowired
    private VentaProductoRepository ventaProductoRepository;

    @Transactional
    public void guardarVenta(List<VentaProductoMensaje> mensaje) {

        List<VentaProducto> productos = new ArrayList<>();

        for (VentaProductoMensaje item : mensaje) {
            VentaProducto producto = new VentaProducto();

            producto.setNombreCliente(item.getNombreCliente());
            producto.setIdCliente(item.getIdCliente());
            producto.setIdProducto(item.getIdProducto());
            producto.setNombreProducto(item.getNombreProducto());
            producto.setPrecio(item.getPrecio());
            producto.setCantidad(item.getCantidad());

            productos.add(producto);
        }

        ventaProductoRepository.saveAll(productos);
    }
}
