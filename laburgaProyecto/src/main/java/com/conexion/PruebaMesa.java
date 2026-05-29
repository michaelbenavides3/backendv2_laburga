package com.conexion;

import com.dao.MesaDao;
import com.modelo.Mesa;
import java.util.List;

public class PruebaMesa {

    public static void main(String[] args) {
        
        System.out.println("=== INICIANDO PRUEBA DE MESAS EN LABUR-GA ===");
        
        // 1. Instanciamos el DAO de las mesas
        MesaDao mesaDao = new MesaDao();
        
        // 2. Ejecutamos el método para sembrar las 8 mesas si la BD está vacía
        mesaDao.inicializarMesasDefault();
        
        System.out.println("\n=== VERIFICANDO LAS MESAS REGISTRADAS ===");
        
        // 3. Listamos las mesas para comprobar en la consola de NetBeans que quedaron melas
        List<Mesa> listaDeMesas = mesaDao.listarMesas();
        
        if (listaDeMesas.isEmpty()) {
            System.out.println("Ojo: No se encontraron mesas en la base de datos.");
        } else {
            for (Mesa m : listaDeMesas) {
                // CAMBIADO AQUÍ: Ahora llama a m.getCapcidMesa()
                System.out.println("ID: " + m.getIdMesas() 
                        + " | Mesa N°: " + m.getNumeroMesa() 
                        + " | Capacidad: " + m.getCapcidadMesa()+ " personas"
                        + " | Estado: " + m.getEstadoMesa());
            }
        }
        
        System.out.println("\n=== FIN DE LA PRUEBA ===");
    }
}