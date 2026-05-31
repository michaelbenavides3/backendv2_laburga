package com.dao;

import com.conexion.claseConexion;
import java.sql.*;

public class CorreoUsuarioDao {

    // metodo para guardar el correo en la bd
    //metodo para validar el correo de un usuario  trabajador
    public boolean insertarCorreo(int idUsuario, String correo) {
        
        /* * explicacion del sql:
         * hago un INSERT INTO para meter un registro nuevo en la tabla 'correoUsuario'.
         * los campos que estoy llenando son 'id_usuario' y 'correo_usuario'.
         * el 'id_usuario' viene de la tabla usuarios y sirve para saber de quien es el correo (es la relacion).
         * el 'correo_usuario' es el dato de texto que metio el usuario en el form.
         * los 'VALUES (?, ?)' son comodines, los pongo asi para que java le pase los datos 
         * y asi evitar que alguien meta codigo malicioso (SQL Injection).
         */
        //cin insert se guarda un correo de usuario y se le inserta al idusuario el corrousuario
        String sql = "INSERT INTO correoUsuario (id_usuario, correo_usuario) VALUES (?, ?)";
        
        try (Connection con = claseConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // le meto los datos a los signos de pregunta
            ps.setInt(1, idUsuario);    // este es el id que se trajo del usuario creado antes
            ps.setString(2, correo);    // este es el correo que escribio el usuario
            
            // ejecuto y si afecto filas es que salio bien, osea true
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            // si peta algo aqui lo cacho y tiro el error al log
            System.err.println("error al meter el correo: " + e.getMessage());
            return false; // fallo la cosa
        }
    }
}