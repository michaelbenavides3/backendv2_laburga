/*

RESPONSABILIDAD

Listar todos los productos registrados en la base de datos
y enviarlos al JSP para que el administrador pueda visualizarlos.



METODO DAO UTILIZADO

ProductosDao

1. obtenerListaTodosLosProductos()

Obtiene todos los productos almacenados
en la base de datos y los devuelve en una lista.

*/

package com.controlador;

import com.dao.ProductosDao;
import com.modelo.Productos;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "ListarProductosControlador",
        urlPatterns = {"/ListarProductosControlador"})
public class ListarProductosControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        PASO 1 CREAR EL DAO
        

        Se crea un objeto ProductosDao para poder acceder a los métodos que realizan consultas sobre la tabla productos.
        */
        ProductosDao productosDao = new ProductosDao();

        /*
        
        PASO 2 OBTENER LA LISTA DE PRODUCTOS
        

        Se llama al método:

        obtenerListaTodosLosProductos()

        Este método consulta la base de datos y devuelve una lista con todos los productos registrados.
        */
        List<Productos> listaProductos = productosDao.obtenerListaTodosLosProductos();

        /*
      
        PASO 3 ENVIAR LA LISTA AL JSP
  

        setAttribute() guarda información dentro del
        objeto request.

        El nombre "listaProductos" será utilizado por
        el JSP para recuperar la información mediante:

        request.getAttribute("listaProductos");
        */
        request.setAttribute("listaProductos", listaProductos);

        /*
        
        PASO 4 ABRIR EL JSP
        

        getRequestDispatcher()

        Busca el archivo JSP que mostrará la información.

        En este caso:

        html/a-listar-productos.jsp
        */
        request.getRequestDispatcher("html/a-listar-productos.jsp")

                /*
                forward()

                Transfiere el control hacia el JSP utilizando
                el mismo request y response.

                Gracias a forward():

                • El JSP recibe el atributo listaProductos.

                • No se crea una nueva petición HTTP.

                • El JSP puede mostrar inmediatamente
                  la lista de productos.

                Si aquí se utilizara sendRedirect(),
                el atributo listaProductos se perdería.
                */
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        Cuando la petición llega por GET,
        simplemente se reutiliza processRequest()
        para evitar duplicar código.
        */
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        Cuando la petición llega por POST,
        también se reutiliza processRequest().

        Así toda la lógica permanece en un solo método.
        */
        processRequest(request, response);
    }
}