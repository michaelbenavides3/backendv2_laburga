
package com.conexion;

import com.dao.ProductosDao;
import com.modelo.Productos;
import java.util.List;

public class PruebaProductos {

    
    public static void main(String[] args) {
       
        System.out.println("inicia la prueba de insercion de productos");
        
        //creamos el pueente para comunicarse con labse de datos
        ProductosDao gestorProductos = new ProductosDao();
        
        // variable testigo para validar los registros (empezamos asumiendo que todo va bien)
        //boolean todoGuardadoExitosamente = true;
        
        //seccion hamburguesas.
        gestorProductos.registrarNuevoProducto(new Productos(0, "Hamburguesa Clasica", "Carne 100% de res, queso amarillo, lechuga, tomate, cebolla y mayonesa en pan brioche.", 19000, "Hamburguesas", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Hamburguesa Especial", "Carne de res, queso cheddar, tocino crujiente, cebolla caramelizada y salsa BBQ.", 25000, "Hamburguesas", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Hamburguesa de Pollo", "Carne de pollo, guacamole, chiles, pico de gallo, queso amarillo y mayonesa de la casa.", 22000, "Hamburguesas", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Hamburguesa Suprema","Doble carne, queso cheddar, tocino crujiente, aros de cebolla, lechuga, tomate y salsa de la casa.", 35000, "Hamburguesas", true));
        
        //seccion de hotdos
        gestorProductos.registrarNuevoProducto(new Productos(0, "Hotdog Clasico","Salchicha Franckfur en pan suave, con mostaza, ketchup, cebolla picada y el toque tradicioanl.", 19000, "Hotdogs", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Hotdog de la Casa","Salchicha premium sobre pan artesanal, bañada en chili con carne, queso y toque de jalapeños.", 19000, "Hotdogs", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Hotdog Americano","Salchicha Frankfurt, pan al vapor, mostaza, chucrut y cebolla dorada, el autentico sabor neoyorquino.", 19000, "Hotdogs", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Hotdog XXL 32cm","Salchicha gigante de 32cm en pan artesanal, doble porcion de inredientes, pollo, carne, salsa de la casa.", 19000, "Hotdogs", true));
        
        //seccion papas locas
        gestorProductos.registrarNuevoProducto(new Productos(0, "Papas Locas","papas crujientes, bañadas en queso, carne, salsa y una explosion de toppings para compartir", 25000, "papaslocas", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Papas Especiales","Papas, salchichas premium, tocineta, salsa de queso, aros de cebolla, mermelada de pimenton.arne, queso cheddar, tocino crujiente, aros de cebolla, lechuga, tomate y salsa de la casa.", 40000, "papslocas", true));
        
        //seccion de otros
        gestorProductos.registrarNuevoProducto(new Productos(0, "Gaseosa","Sabores productos postobon.", 5000, "otros", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Botella de Agua","Botella de agua con gas o sin gas.", 5000, "otros", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Te Hatsu","Varios sabores.", 5000, "otros", true));
        gestorProductos.registrarNuevoProducto(new Productos(0, "Cervezas","Importadas.", 12000, "otros", true));
        
        
    }
    
}
