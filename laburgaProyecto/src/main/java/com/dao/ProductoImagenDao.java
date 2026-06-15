package com.dao;

import com.conexion.claseConexion;
import com.modelo.ProductoImagen;
import com.modelo.Productos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*


            METODO 1. REGISTRAR IMAGEN producto


            METODO 2. OBTENER IMAGEN DE PRODUCTO

            METODO 3. LISTAR IMAGENES DE PRODUCTO


            METODO 4. DESACTIVAR IMAGENES ANTERIORES

            METODO 5. ACTIVAR IMAGEN

            METODO 6. OBETNER RUTA IMAGEN PRODUCTO


 */
public class ProductoImagenDao {

    /*
    
    METODO 1. REGISTRAR IMAGEN
    
     */
    public boolean registrarImagenProducto(ProductoImagen nuevaImagenObjeto) {

        String consultaSql = """
            INSERT INTO productoImagenes
            (
                id_producto,
                ruta_archivoImagen,
                imagen_actualizada
            )
            VALUES (?, ?, ?)
            """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql);) {

            sentenciaSql.setInt(1, nuevaImagenObjeto.getIdProducto());

            sentenciaSql.setString(2, nuevaImagenObjeto.getRutaArchivoImagen());

            sentenciaSql.setBoolean(3, nuevaImagenObjeto.isImagenActualizada());

            return sentenciaSql.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Error registrando imagen: " + e.getMessage());

            return false;
        }
    }

    /*
    
    METODO 2. OBTENER IMAGEN DE PRODUCTO
    
     */
    public ProductoImagen obtenerImagenProducto(int idProducto) {

        String consultaSql = """
            SELECT *
            FROM productoImagenes
            WHERE id_producto = ?
            AND imagen_actualizada = true
            LIMIT 1
            """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql);) {

            sentenciaSql.setInt(1, idProducto);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                ProductoImagen imagen = new ProductoImagen();

                imagen.setIdProductoImagenes(resultadoConsulta.getInt("id_productoImagenes"));

                imagen.setIdProducto(resultadoConsulta.getInt("id_producto"));

                imagen.setRutaArchivoImagen(resultadoConsulta.getString("ruta_archivoImagen"));

                imagen.setImagenActualizada(resultadoConsulta.getBoolean("imagen_actualizada"));

                return imagen;
            }

        } catch (Exception e) {

            System.out.println("Error obteniendo imagen: " + e.getMessage());
        }

        return null;
    }

    /*
    
    METODO 3. LISTAR IMAGENES DE PRODUCTO
   
     */
    public List<ProductoImagen> listarImagenesProducto(int idProducto) {

        List<ProductoImagen> listaImagenes = new ArrayList<>();

        String consultaSql = """
            SELECT *
            FROM productoImagenes
            WHERE id_producto = ?
            """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql);) {

            sentenciaSql.setInt(1, idProducto);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            while (resultadoConsulta.next()) {

                ProductoImagen imagen = new ProductoImagen();

                imagen.setIdProductoImagenes(resultadoConsulta.getInt("id_productoImagenes"));

                imagen.setIdProducto(resultadoConsulta.getInt("id_producto"));

                imagen.setRutaArchivoImagen(resultadoConsulta.getString("ruta_archivoImagen"));

                imagen.setImagenActualizada(resultadoConsulta.getBoolean("imagen_actualizada"));

                listaImagenes.add(imagen);
            }

        } catch (Exception e) {

            System.out.println("Error listando imagenes: " + e.getMessage());
        }

        return listaImagenes;
    }

    /*
   
    METODO 4. DESACTIVAR IMAGENES ANTERIORES
    
     */
    public boolean desactivarImagenesProducto(int idProducto) {

        String consultaSql = """
            UPDATE productoImagenes
            SET imagen_actualizada = false
            WHERE id_producto = ?
            """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql);) {

            sentenciaSql.setInt(1, idProducto);

            sentenciaSql.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println("Error desactivando imagenes: " + e.getMessage());

            return false;
        }
    }

    /*
    
    METODO 5. ACTIVAR IMAGEN
   
     */
    public boolean activarImagenProducto(int idProductoImagenes) {

        String consultaSql = """
            UPDATE productoImagenes
            SET imagen_actualizada = true
            WHERE id_productoImagenes = ?
            """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql);) { sentenciaSql.setInt(1, idProductoImagenes);

            return sentenciaSql.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Error activando imagen: " + e.getMessage());

            return false;
        }
    }
    
    /*
     * MEETODO 6 OBTENER RUTA IMAGEN PRODUCTO 
     */

    public String obtenerRutaImagenProducto(  int idProducto) {

        String consultaSql
                = """
        SELECT ruta_archivoImagen
        FROM productoImagenes
        WHERE id_producto = ?
        AND imagen_actualizada = true
        LIMIT 1
        """;

        try (
                Connection conexion  = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement( consultaSql);) {

            sentenciaSql.setInt( 1, idProducto);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                return resultadoConsulta.getString("ruta_archivoImagen");
            }

        } catch (Exception e) {

            System.out.println( "Error obteniendo imagen: "+ e.getMessage());
        }

        return "";
    }

}
