package com.conexion;

import com.dao.ClienteDao;
import com.modelo.Cliente;

public class PruebaCliente {
    
    public static void main(String[] args) {
        
        System.out.println("===   INICIANDO PRUEBA  10 INSERCIONES   ==");
        

        // Instanciamos el DAO que contiene tu lógica transaccional en cascada
        ClienteDao dao = new ClienteDao();

        // Creamos un arreglo de objetos tipo Cliente con 10 registros ficticios listos para Labur-GA
        // Estructura: Nombre Completo, Documento (CC), Teléfono (Máx 10 caracteres), Correo
        Cliente[] listaClientesPrueba = {
            new Cliente("Carlos Mendoza", "1098200301", "3151112223", "carlos@gmail.com"),
            new Cliente("Diana Marcela Gomez", "1098200302", "3164445556", "diana@outlook.com"),
            new Cliente("Andres Felipe Celis", "1098200303", "3207778889", "andres@hotmail.com"),
            new Cliente("Sandra Milena Ardila", "1098200304", "3102223334", "sandra@gmail.com"),
            new Cliente("Javier Rincon", "1098200305", "3185556667", "javier@laburga.com"),
            new Cliente("Ingrid Johana Prada", "1098200306", "3128889990", "ingrid@gmail.com"),
            new Cliente("Pedro Alonzo Torres", "1098200307", "3001112223", "pedro@outlook.com"),
            new Cliente("Laura Valentina Perez", "1098200308", "3144445556", "laura@gmail.com"),
            new Cliente("Gustavo Petro Silva", "1098200309", "3177778889", "gustavo@hotmail.com"),
            new Cliente("Karen Sleent Carrillo", "1098200310", "3112223334", "karen@laburga.com")
        };

        // Variables contadoras para sacar la estadística final en consola
        int insercionesExitosas = 0;
        int insercionesFallidas = 0;

        // Bucle 'for' para recorrer e insertar de forma automática los 10 clientes
        for (int i = 0; i < listaClientesPrueba.length; i++) {
            Cliente temporal = listaClientesPrueba[i];
            
            System.out.print("Procesando registro [" + (i + 1) + "/10] -> " + temporal.getNombreCompleto() + "... ");
            
            // Ejecutamos tu método transaccional
            boolean resultadoCircuito = dao.registrarClienteCompleto(temporal);
            
            if (resultadoCircuito) {
                System.out.println("¡ÉXITO! (Tablas: clientes, telefono y correo actualizadas)");
                insercionesExitosas++;
            } else {
                System.out.println("¡FALLÓ! (Revisa duplicidad de CC o longitud de datos)");
                insercionesFallidas++;
            }
        }

        // == REPORTE FINAL DEL CONTROL DE CALIDAD ==
        System.out.println("\n==================================================");
        System.out.println("===          RESUMEN DE LA PRUEBA DE FUEGO      ==");
        System.out.println("==================================================");
        System.out.println(" Registros Exitosos: " + insercionesExitosas + " / 10");
        System.out.println(" Registros Fallidos: " + insercionesFallidas + " / 10");
        System.out.println("==================================================");
        
    }
}