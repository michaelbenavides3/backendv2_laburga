package com.dao;

import com.conexion.claseConexion;
import java.sql.*;

public class TelefonoUsuarioDao {

    public boolean insertarTelefono(int idUsuario, String numero) {
        String sql = "INSERT INTO telefonoUsuarios (id_usuario, telefono) VALUES (?, ?)";
        try (Connection con = claseConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, numero);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar teléfono: " + e.getMessage());
            return false;
        }
    }
}