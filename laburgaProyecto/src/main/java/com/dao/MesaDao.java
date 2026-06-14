
/*
    
responsbailidad: administra el estado y la informacion de las mesas del restaurante

    - 1. METODO PARA INICIALIZAR LA MESA POR DEFAULT    --> crea las mesas iniciales del restaurante por defecto 8

    - 2. METODO PARA LISTAR LAS MESAS --> obtiene todas las mesas registrda con exito

    - 3. METODO PARA CAMBIAR DE ESTADO DE LA MESA --> camibia el estado de una mesa (disponible, ocupada, pendienteCobro)
*/


package com.dao;

import com.conexion.claseConexion;
import com.modelo.Mesa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDao {
    
    
    /*
    
    
    
    1. METODO PARA INICIALIZAR LA MESA POR DEFAULT
    */

    // metodo para crear las mesas si no existen (solo se corre al inicio)
    public void inicializarMesasDefault() {
        Connection accesoBD = claseConexion.getConexion();
        
        try {
            // primero pregunto si ya hay mesas para no duplicar datos
            String sqlCheck = "SELECT COUNT(*) FROM mesas";
            //(select)indicamos que queremos obtener (count) cuenta cada fila sin importar si tiene null
            PreparedStatement verificar = accesoBD.prepareStatement(sqlCheck);
            ResultSet rs = verificar.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // si la tabla esta vacia (0), inserto las 8 mesas por defecto
                String sqlInsert = "INSERT INTO mesas (numero_mesa, capcidad_mesa, estado_mesa) VALUES "
                        + "(1, 4, 'disponible'), (2, 4, 'disponible'), (3, 2, 'disponible'), "
                        + "(4, 6, 'disponible'), (5, 4, 'disponible'), (6, 4, 'disponible'), "
                        + "(7, 2, 'disponible'), (8, 8, 'disponible')";
                
                PreparedStatement insertar = accesoBD.prepareStatement(sqlInsert);
                insertar.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    /*
    
    
    
     2. METODO PARA LISTAR LAS MESAS
    */
    // metodo para traer todas las mesas y ver si estan ocupadas o libres
    public List<Mesa> listarMesas() {
        List<Mesa> lista = new ArrayList<>();
        String sql = "SELECT id_mesas, numero_mesa, capcidad_mesa, estado_mesa FROM mesas";

        try (Connection con = claseConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mesa m = new Mesa();
                m.setIdMesas(rs.getInt("id_mesas"));
                m.setNumeroMesa(rs.getInt("numero_mesa"));
                m.setCapcidadMesa(rs.getInt("capcidad_mesa"));
                m.setEstadoMesa(rs.getString("estado_mesa"));
                lista.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    
    
    /*
    
    
    3. METODO PARA CAMBIAR DE ESTADO DE LA MESA
    */

    // metodo para cambiar el estado (ejemplo: pasar de 'disponible' a 'ocupada')
    public void cambiarEstado(int idMesa, String nuevoEstado) {
        // el UPDATE cambia solo el estado de la mesa que coincida con el id que le mando
        String sql = "UPDATE mesas SET estado_mesa = ? WHERE id_mesas = ?";

        try (Connection con = claseConexion.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado); // el nuevo estado (ej. 'ocupada')
            ps.setInt(2, idMesa);         // la mesa que quiero cambiar
            System.out.println("Actualizando mesa "+ idMesa+ " a estado "+ nuevoEstado);
            int filasActualizadas = ps.executeUpdate();
            System.out.println("Filas actualizadas mesa: " + filasActualizadas);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}