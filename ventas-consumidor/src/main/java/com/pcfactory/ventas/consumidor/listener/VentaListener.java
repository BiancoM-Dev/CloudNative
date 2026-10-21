package com.pcfactory.ventas.consumidor.listener;

import com.pcfactory.ventas.consumidor.dto.VentaProductoMensaje;
import com.pcfactory.ventas.consumidor.service.VentaProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VentaListener {

    private static final Logger logger =
            LoggerFactory.getLogger(VentaListener.class);

    @Autowired
    private VentaProductoService ventaProductoService;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void consumirVenta(List<VentaProductoMensaje> mensaje) {

        ventaProductoService.guardarVenta(mensaje);

        logger.info(
                "Venta procesada con exito: {} productos guardados en MySQL",
                mensaje.size()
        );
    }
}
