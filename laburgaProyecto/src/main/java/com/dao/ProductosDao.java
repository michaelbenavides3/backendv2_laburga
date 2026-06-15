
/*
    - 1. METODO PARA REGISTRAR NUEVO PRODUCTO --> registrar nuevo producto

    - 2. METODO PARA OBTENER LA LISTA DE LOS PRODUCTOS -->  obtener todos los productos registrados en la base de dats

    - 3. MERODO OBTENER PRODUCTOS POR ID --> con este metodo vamos a obtener el id del producto

    - 4. MERODO ACTUALIZAR PRODUCTO  --> permite modificar los datos de un productos existente

    - 5. MERODO CAMBIAR DISPONIBILIDAD PRODUCTO  --> permite activar o desactivar un producto
 */
package com.dao;

import com.conexion.claseConexion;
import com.modelo.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductosDao {

    /*
        - 1. METODO PARA REGISTRAR NUEVO PRODUCTO
    
        - su funcion principal es registrar un nuevo producto dentro del menu (metodo que todavia no esta funcionado, o si funciona pero se utiliza en pruebaprodcutos)
     */
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

    /*
        2. METODO PARA OBTENER LA LISTA DE LOS PRODUCTOS
    
    
        - su funcion principal es por medio de una lista obtener todos los productos registrados en la base de dats
        
     */
    // TRAER LA LISTA DE TODOS LOS PRODUCTOS
    public List<Productos> obtenerListaTodosLosProductos() {

        // Creamos una lista vacía para ir metiendo los platos que encontremos
        List<Productos> listaDeProductosEncontrados = new ArrayList<>();

        Connection conexionFisicaBaseDatos = null;
        PreparedStatement sentenciaSqlPreparada = null;
        ResultSet filasResultadosSql = null; // Aquí se guardará lo que responda MySQL

        // La orden para pedirle todos los productos a la base de datos
        //con select traemos todo los detalle productos de la carta 
        String consultaSeleccionarSql = "SELECT id_producto, nombre_producto, descripcion_producto, precio_baseproducto, categoria_producot, disponible_producto FROM productos";

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
                    productoTemporalEncontrado.setPrecioBaseProducto(filasResultadosSql.getDouble("precio_baseproducto"));
                    productoTemporalEncontrado.setCategoriaProducto(filasResultadosSql.getString("categoria_producot"));
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

    /*
    3. METODO PARA OBTENER PRODUCTO POR ID
     */
    public Productos obtenerProductoPorId(int idProducto) {

        Productos productoEncontrado = null;

        /*
            select * le esta indicando que se traiga todo las columnas de esta tabla
            from indica el origen de la tabla productos
            con where le dice traiga el id_producto 
        
        
         */
        String sql = """  
        SELECT *
        FROM productos
        WHERE id_producto = ?
        """;

        try (
                Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            ResultSet consulta = ps.executeQuery();

            if (consulta.next()) {

                productoEncontrado = new Productos();

                productoEncontrado.setIdProducto(consulta.getInt("id_producto"));

                productoEncontrado.setNombreProducto(consulta.getString("nombre_producto"));

                productoEncontrado.setDescripcionProducto(consulta.getString("descripcion_producto"));

                productoEncontrado.setPrecioBaseProducto(consulta.getDouble("precio_baseproducto"));

                productoEncontrado.setCategoriaProducto(consulta.getString("categoria_producot"));

                productoEncontrado.setDisponibleProducto(consulta.getBoolean("disponible_producto"));
            }

        } catch (Exception e) {

            System.out.println("Error al obtener producto: " + e.getMessage());
        }

        return productoEncontrado;
    }

    /*
    4. METODO PARA ACTUALIZAR PRODUCTO

    - permite modificar los datos de un producto existente
    - se utiliza cuando el administrador edita un producto
     */
    public boolean actualizarProducto(Productos productoActualizadoObjeto) {

        boolean operacionActualizacionExitosa = false;

        /*
            con el update se le indica que se quiere modificar los que se tiene guardados dentro de la tabla,
            set cambiar el nobre actal del producto, por el nuevo, lo imso pra descripcion perco y ctegoria
            con el where, le indica que se le apliquen todos los cambios unicamente al producot cuyo id coindicda
         */
        String consultaActualizarProductoSql = """
        UPDATE productos
        SET nombre_producto = ?,
            descripcion_producto = ?,
            precio_baseproducto = ?,
            categoria_producot = ?
        WHERE id_producto = ?
    """;

        try (
                Connection conexionFisicaBaseDatos = claseConexion.getConexion(); PreparedStatement sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaActualizarProductoSql)) {

            // nombre del producto
            sentenciaSqlPreparada.setString(1, productoActualizadoObjeto.getNombreProducto());

            // descripcion del producto
            sentenciaSqlPreparada.setString(2, productoActualizadoObjeto.getDescripcionProducto());

            // precio base
            sentenciaSqlPreparada.setDouble(3, productoActualizadoObjeto.getPrecioBaseProducto());

            // categoria
            sentenciaSqlPreparada.setString(4, productoActualizadoObjeto.getCategoriaProducto());

            // id del producto que se va a modificar
            sentenciaSqlPreparada.setInt(5, productoActualizadoObjeto.getIdProducto());

            int cantidadFilasActualizadas = sentenciaSqlPreparada.executeUpdate();

            if (cantidadFilasActualizadas > 0) {

                operacionActualizacionExitosa = true;

                System.out.println("Producto actualizado correctamente");
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println("Error al actualizar producto: " + errorBaseDatos.getMessage());
        }

        return operacionActualizacionExitosa;
    }

    /*
    5. METODO PARA CAMBIAR DISPONIBILIDAD DEL PRODUCTO

    - permite activar o desactivar un producto
    - no elimina el producto de la base de datos
    - evita afectar pedidos y facturas antiguas
     */
    public boolean cambiarDisponibilidadProducto(int identificadorProducto, boolean nuevoEstadoDisponibilidad) {

        boolean operacionExitosa = false;

        //actualizamos el estado de disponibilidad del producto
        String consultaActualizarDisponibilidad
                = "UPDATE productos "
                + "SET disponible_producto = ? "
                + "WHERE id_producto = ?";

        try (
                Connection conexionFisicaBaseDatos = claseConexion.getConexion(); PreparedStatement sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement( consultaActualizarDisponibilidad)) {

            sentenciaSqlPreparada.setBoolean( 1, nuevoEstadoDisponibilidad);

            sentenciaSqlPreparada.setInt( 2,identificadorProducto);

            int cantidadFilasActualizadas = sentenciaSqlPreparada.executeUpdate();

            if (cantidadFilasActualizadas > 0) {

                operacionExitosa = true;

                System.out.println( "Disponibilidad del producto actualizada correctamente");
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println( "Error al cambiar disponibilidad del producto: " + errorBaseDatos.getMessage());
        }

        return operacionExitosa;
    }
}
