package com.conexion;

// Se importan las herramientas de java para manejar conexión a base de datos
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class claseConexion {
    // Se define la dirección de mi servidor en mysql (Base de datos: restauranteLaburGA)
    private static final String URL = "jdbc:mysql://localhost:3306/restauranteLaburGA";
    // Se define el usuario de administrador
    private static final String USER = "root";
    // Se define la contraseña de la base de datos
    //private static final String PASSWORD = "Lauraluna94.";
    private static final String PASSWORD = "#Aprendiz2024";
    
    // Creamos el método principal para obtener la conexión. Se deja
    // static para poder llamarlo directamente desde el DAO sin crear objetos.
    public static Connection getConexion() {
        
        // Creo una variable vacía para guardar la conexión más adelante.
        // Empieza en null para limpiarla antes de conectarme a la base de datos.
        Connection conexion = null;
        
        try {
            // Se activa el traductor moderno de mysql (Compatible con Maven mysql-connector-j)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Si todo coincide, la comunicación se guarda dentro de mi variable conexion
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Si funciona y se abre con éxito, se imprime este mensaje
            System.out.println("¡ conexion exitosa a la base de datos ! :D ");
            
        } catch (ClassNotFoundException errorEstructura) {
            // Si el programa no encuentra el conector de tu proyecto
            System.out.println("error: no se encontro el driver de mysql: " + errorEstructura.getMessage());
        } catch (SQLException errorBaseDatos) {
            // Si los datos de acceso, puerto o el nombre de la base de datos están mal
            System.out.println("error: no se puede conectar a la base de datos porque los datos estan mal " + errorBaseDatos.getMessage());
        }
        
        // Al final regreso la variable conexion. Si todo sale bien tendrá la comunicación lista, si falla devolverá null.
        return conexion;
    }   
}