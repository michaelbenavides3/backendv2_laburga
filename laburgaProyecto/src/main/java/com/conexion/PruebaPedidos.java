package com.conexion;

import com.dao.PedidoDao;
import com.modelo.Pedido;

public class PruebaPedidos {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBA DE REGISTRO DE PEDIDO ===");

        // 1. Instanciamos el DAO del pedido
        PedidoDao pedidoDao = new PedidoDao();

        // 2. Creamos un objeto Pedido usando el constructor vacío
        Pedido pedidoPrueba = new Pedido();
        
        // 3. Asignamos los valores usando los métodos Setter
        pedidoPrueba.setIdPedido(0); // 0 porque es AUTO_INCREMENT
        pedidoPrueba.setIdMesa(1);
        pedidoPrueba.setIdMesero(3);
        pedidoPrueba.setEstadoPedido("activo");

        System.out.println("Enviando datos a MySQL: Mesa " + pedidoPrueba.getIdMesa() + " - Mesero " + pedidoPrueba.getIdMesero());

        // 4. Ejecutamos el método del DAO (asegúrate de tener este método en PedidoDao)
        int idPedidoGenerado = pedidoDao.registrarNuevoPedido(pedidoPrueba);

        // 5. Verificamos la respuesta
        if (idPedidoGenerado > 0) {
            System.out.println("¡ÉXITO TOTAL! El pedido se registró correctamente en la base de datos.");
        } else {
            System.out.println("¡ERROR! Revisa la consola para ver el error de SQL.");
        }
    }
}