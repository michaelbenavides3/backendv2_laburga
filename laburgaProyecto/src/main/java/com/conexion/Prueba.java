package com.conexion;

// Importo la herramienta de conexión para poder verificarla aquí adentro.
import java.sql.Connection;

public class Prueba {

    // Creo el método principal "main" que sirve como el botón de encendido para arrancar esta prueba.
    public static void main(String[] args) {
        
        // Imprimo un mensaje inicial para saber que el programa de prueba comenzó a correr.
        System.out.println("Iniciando la prueba de conexión...");
        
        // Llamo al método "getConexion" de mi clase anterior y el resultado lo guardo en una variable.
        Connection miConexionTemporal = claseConexion.getConexion();
        
        // Reviso si mi variable no quedó vacía (es decir, si es diferente de null).
        if (miConexionTemporal != null) {
            
            // Si no está vacía, significa que la conexión física con la base de datos se logró.
            System.out.println(">>> ¡CONFIRMADO! Tu Java ya habla con la base de datos restauranteLaburGA. <<<");
            
            try {
                // Como buena práctica, cierro la conexión de prueba para no dejar canales abiertos en el servidor.
                miConexionTemporal.close();
                System.out.println("Conexión de prueba cerrada correctamente.");
            } catch (Exception errorAlCerrar) {
                System.out.println("Error al cerrar la conexión: " + errorAlCerrar.getMessage());
            }
            
        } else {
            // Si la variable se quedó en "null", significa que el código falló en la sección del try-catch.
            System.out.println(">>> ERROR: La conexión devolvió un valor vacío. Revisa los mensajes de arriba. <<<");
        }
    }
}
