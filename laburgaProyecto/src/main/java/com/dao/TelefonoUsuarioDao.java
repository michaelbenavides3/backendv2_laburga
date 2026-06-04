
/*
    1. METODO INSERTA TELEFONO --> se inserta un telefono para asociar a un usuaroio
*/


package com.dao;

import com.conexion.claseConexion;
import java.sql.*;

public class TelefonoUsuarioDao {
        // metodo para guardar el numero de telefono de un usuario en la bd
    
    
    /*
    1. METODO INSERTA TELEFONO
    
    -este metodo en un boolean nos permite llevaer el contro del telefono si lo registra es true si no lo regisra es false
    -su funciion princial es registar un telefono y asociarlo a un usuario
    -su proeceos es: recibe el id del usuario creado, recibe el numero digitado ejecuta el insetr gurada la relacion usuario-telefono retorna el resultado de la oprecion
    */

    public boolean insertarTelefono(int idUsuario, String numero) {
        /* :
         * hago un INSERT INTO para registrar un telefono nuevo en 'telefonoUsuarios'.
         * guardo el 'id_usuario' para saber a qué persona pertenece este número (relación).
         * guardo el 'telefono' con el valor que viene desde la interfaz.
         * los signos '?' son para evitar inyecciones SQL, haciendo que la consulta sea segura.
         */
        String sql = "INSERT INTO telefonoUsuarios (id_usuario, telefono) VALUES (?, ?)";
        // abro la conexión usando la clase de configuración y preparo la sentencia
        try (Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, numero);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar teléfono: " + e.getMessage());
            return false;
        }
    }
}
