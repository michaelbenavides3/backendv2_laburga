package com.dao;

import com.conexion.claseConexion;
import com.conexion.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductosDao {

    
    // GUARDAR UN NUEVO PRODUCTO EN LA BASE DE DATOS
    
    public boolean registrarNuevoProducto(Productos nuevoProductoObjeto) {
        
        Connection conexionFisicaBaseDatos = null;
        PreparedStatement sentenciaSqlPreparada = null;
        
        // Esta variable nos dirá al final si se guardó o no el producto
        boolean operacionRegistroExitosa = false;

        // La orden para insertar en MySQL. Los "?" son cajas vacías que llenaremos luego.
        String consultaInsertarSql = "INSERT INTO productos (nombre_producto, descripcion_producto, precio_baseproducto, categoria_producot, disponible_producto) VALUES (?, ?, ?, ?, ?)";

        try {
            // 1. Abrimos la puerta a la base de datos
            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {
                
                // 2. Le preparamos la orden a MySQL
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaInsertarSql);

                // 3. Llenamos las cajas vacías (?) con los datos del producto
                sentenciaSqlPreparada.setString(1, nuevoProductoObjeto.getNombreProducto());
                sentenciaSqlPreparada.setString(2, nuevoProductoObjeto.getDescripcionProducto());
                sentenciaSqlPreparada.setDouble(3, nuevoProductoObjeto.getPrecioBaseProducto());
                sentenciaSqlPreparada.setString(4, nuevoProductoObjeto.getCategoriaProducto());
                sentenciaSqlPreparada.setBoolean(5, nuevoProductoObjeto.isDisponibleProducto());

                // 4. Mandamos la orden. Si nos devuelve un número mayor a 0, es porque se guardó.
                int cantidadFilasAfectadas = sentenciaSqlPreparada.executeUpdate();

                if (cantidadFilasAfectadas > 0) {
                    operacionRegistroExitosa = true; // Todo salió bien
                    System.out.println("El nuevo producto se guardó en la base de datos.");
                }
            }

        } catch (SQLException errorBaseDatos) {
            // Si algo falla, aquí nos enteramos de qué pasó
            System.out.println("No se pudo guardar el producto. Motivo: " + errorBaseDatos.getMessage());
            
        } finally {
            // 5. Pase lo que pase, cerramos todo para no dejar la puerta abierta ni gastar memoria
            try {
                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("Conexión de registro cerrada de forma segura.");
                }
            } catch (SQLException errorAlCerrar) {
                System.out.println("Error al cerrar los canales: " + errorAlCerrar.getMessage());
            }
        }

        // Devolvemos true o false según el resultado
        return operacionRegistroExitosa;
    }

    
    // TRAER LA LISTA DE TODOS LOS PRODUCTOS
    
    public List<Productos> obtenerListaTodosLosProductos() {
        
        // Creamos una lista vacía para ir metiendo los platos que encontremos
        List<Productos> listaDeProductosEncontrados = new ArrayList<>();
        
        Connection conexionFisicaBaseDatos = null;
        PreparedStatement sentenciaSqlPreparada = null;
        ResultSet filasResultadosSql = null; // Aquí se guardará lo que responda MySQL

        // La orden para pedirle todos los productos a la base de datos
        String consultaSeleccionarSql = "SELECT id_producto, nombre_producto, descripcion_producto, precio_base_producto, categoria_producto, disponible_producto FROM productos";

        try {
            // 1. Abrimos la conexión
            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {
                
                // 2. Preparamos y ejecutamos la consulta de lectura
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaSeleccionarSql);
                filasResultadosSql = sentenciaSqlPreparada.executeQuery();

                // 3. Recorremos los resultados fila por fila
                while (filasResultadosSql.next()) {
                    
                    // Creamos un producto en blanco para llenarlo con los datos de esta fila
                    Productos productoTemporalEncontrado = new Productos();
                    
                    productoTemporalEncontrado.setIdProducto(filasResultadosSql.getInt("id_producto"));
                    productoTemporalEncontrado.setNombreProducto(filasResultadosSql.getString("nombre_producto"));
                    productoTemporalEncontrado.setDescripcionProducto(filasResultadosSql.getString("descripcion_producto"));
                    productoTemporalEncontrado.setPrecioBaseProducto(filasResultadosSql.getDouble("precio_base_producto"));
                    productoTemporalEncontrado.setCategoriaProducto(filasResultadosSql.getString("categoria_producto"));
                    productoTemporalEncontrado.setDisponibleProducto(filasResultadosSql.getBoolean("disponible_producto"));
                    
                    // Agregamos el producto ya lleno a nuestra lista
                    listaDeProductosEncontrados.add(productoTemporalEncontrado);
                }
            }

        } catch (SQLException errorBaseDatos) {
            System.out.println("No se pudo traer la lista. Motivo: " + errorBaseDatos.getMessage());
        } finally {
            // 4. Limpiamos y cerramos todo para cuidar el servidor
            try {
                if (filasResultadosSql != null) {
                    filasResultadosSql.close();
                }
                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("Conexión de lectura cerrada de forma segura.");
                }
            } catch (SQLException errorAlCerrar) {
                System.out.println("Error al cerrar los canales: " + errorAlCerrar.getMessage());
            }
        }

        // Entregamos la lista final (con o sin productos)
        return listaDeProductosEncontrados;
    }
}