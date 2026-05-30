package com.dao;

import com.conexion.claseConexion;
import com.modelo.Mesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MesaDao {

    public void inicializarMesasDefault() {
        Connection accesoBD = claseConexion.getConexion();
        PreparedStatement verificar = null;
        PreparedStatement insertar = null;
        ResultSet rs = null;

        try {
            String sqlCheck = "SELECT COUNT(*) FROM mesas";
            verificar = accesoBD.prepareStatement(sqlCheck);
            rs = verificar.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Cambiado 'capacidad_mesa' por 'capcid_mesa' en el String de inserción
                String sqlInsert = "INSERT INTO mesas (numero_mesa, capcidad_mesa, estado_mesa) VALUES "
                        + "(1, 4, 'disponible'), "
                        + "(2, 4, 'disponible'), "
                        + "(3, 2, 'disponible'), "
                        + "(4, 6, 'disponible'), "
                        + "(5, 4, 'disponible'), "
                        + "(6, 4, 'disponible'), "
                        + "(7, 2, 'disponible'), "
                        + "(8, 8, 'disponible')";

                insertar = accesoBD.prepareStatement(sqlInsert);
                insertar.executeUpdate();
                System.out.println("=== ¡ÉXITO! 8 Mesas creadas correctamente en MySQL ===");
            }
        } catch (Exception e) {
            System.out.println("Error al inicializar mesas: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (verificar != null) {
                    verificar.close();
                }
            } catch (Exception e) {
            }
            try {
                if (insertar != null) {
                    insertar.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public List<Mesa> listarMesas() {
        List<Mesa> lista = new ArrayList<>();
        Connection accesoBD = claseConexion.getConexion();
        PreparedStatement operacion = null;
        ResultSet resultado = null;

        // Consutal dinamica si encunetra un pedido en pendiente la marca como ocupada automaticamente
        // Consulta limpia, plana y segura (Trae los datos directo de la tabla mesas)
        String sqlQuery = "SELECT id_mesas, numero_mesa, capcidad_mesa, estado_mesa FROM mesas";

        try {
            operacion = accesoBD.prepareStatement(sqlQuery);
            resultado = operacion.executeQuery();

            while (resultado.next()) {
                Mesa m = new Mesa();
                m.setIdMesas(resultado.getInt("id_mesas"));
                m.setNumeroMesa(resultado.getInt("numero_mesa"));
                // Cambiado para que lea la columna exacta de la BD
                m.setCapcidadMesa(resultado.getInt("capcidad_mesa"));
                //aquie se lee la columna calcualada por mysql
                m.setEstadoMesa(resultado.getString("estado_mesa"));
                lista.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error al listar mesas: " + e.getMessage());
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
            } catch (Exception e) {
            }
            try {
                if (operacion != null) {
                    operacion.close();
                }
            } catch (Exception e) {
            }
        }
        return lista;
    }

    public void cambiarEstado(int idMesa, String nuevoEstado) {

        //Connection accesoBD = claseConexion.getConexion();
        //modificar los datos de la tabla mesa ? es el espacio vacia where id_mesas llama solo al id que se le hace el cambio 
        String sqlConsulta = "UPDATE mesas SET estado_mesa = ? WHERE id_mesas = ?";

        try (Connection accesoBD = claseConexion.getConexion(); PreparedStatement operacion = accesoBD.prepareStatement(sqlConsulta)) {

            operacion.setString(1, nuevoEstado);
            operacion.setInt(2, idMesa);

            operacion.executeUpdate();
            System.out.println("Estado de mesa " + idMesa + " actualizado a: " + nuevoEstado + " ===");

        } catch (Exception e) {
            //System.out.println("Error al actualizar estado de mesa: " + e.getMessage());
            e.printStackTrace();
        }

    }
//metodo para listar mesas pendietne de cobro

    public List<Mesa> listarMesasPendientesCobro() {
        List<Mesa> lista = new ArrayList<>();
        String sql = "SELECT * FROM mesas WHERE estado_mesa = 'pendiente_cobro'";
        try {
            // Conexión y ejecución de tu consulta SQL...
            // (Usa la misma estructura que ya tienes en listarMesas())
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

   

}
