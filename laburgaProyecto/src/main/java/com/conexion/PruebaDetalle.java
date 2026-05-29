package com.conexion;

import com.dao.DetallePedidoDao;
import com.modelo.DetallePedido;

public class PruebaDetalle {
    public static void main(String[] args) {
        // 1. Instanciamos el DAO
        DetallePedidoDao dao = new DetallePedidoDao();
        
        // 2. Creamos un objeto con datos de prueba
        // (Asegúrate de que el idPedido 1 exista en tu tabla 'pedidos')
        DetallePedido nuevoDetalle = new DetallePedido();
        nuevoDetalle.setIdPedido(1); 
        nuevoDetalle.setIdProducto(2); 
        nuevoDetalle.setCantidad(3);
        nuevoDetalle.setPrecioVenta(25000.0);
        nuevoDetalle.setObservaciones("Sin cebolla");

        // 3. Ejecutamos el registro
        boolean exito = dao.registrarDetalle(nuevoDetalle);
        
        // 4. Verificamos el resultado
        if (exito) {
            System.out.println("¡Éxito! El detalle se guardó correctamente en la base de datos.");
        } else {
            System.out.println("Error: No se pudo guardar el detalle.");
        }
    }
}