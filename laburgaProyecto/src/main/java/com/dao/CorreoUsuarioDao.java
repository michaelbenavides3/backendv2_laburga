package com.dao;

import com.conexion.claseConexion;
import java.sql.*;

public class CorreoUsuarioDao {

    public boolean insertarCorreo(int idUsuario, String correo) {
        String sql = "INSERT INTO correoUsuario (id_usuario, correo_usuario) VALUES (?, ?)";
        try (Connection con = claseConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, correo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar correo: " + e.getMessage());
            return false;
        }
    }
}