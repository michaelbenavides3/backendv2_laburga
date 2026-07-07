package com.controlador;

import com.dao.ClienteDao;
import com.modelo.Cliente;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BuscarClientePorTelefono")
public class BuscarClientePorTelefonoControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
        1. RECUPERAR EL TELÉFONO INGRESADO POR EL MESERO
        */
        String telefonoBuscado = request.getParameter("telefono");

        /*
        2. RECUPERAR LOS DEMÁS DATOS DEL FORMULARIO
           para no perderlos cuando regresemos al JSP
        */
        String mesa     = request.getParameter("mesa");
        String fecha    = request.getParameter("fecha");
        String hora     = request.getParameter("hora");
        String personas = request.getParameter("personas");
        String ocasion  = request.getParameter("ocasion");

        /*
        3. BUSCAR EL CLIENTE EN LA BASE DE DATOS
        */
        Cliente clienteEncontrado = null;

        if (telefonoBuscado != null && !telefonoBuscado.trim().isEmpty()) {
            ClienteDao clienteDao = new ClienteDao();
            clienteEncontrado = clienteDao.buscarClientePorTelefono(telefonoBuscado.trim());
        }

        /*
        4. MANDAR TODO AL JSP
        */
        request.setAttribute("clienteEncontrado", clienteEncontrado);
        request.setAttribute("telefonoBuscado",   telefonoBuscado);
        request.setAttribute("mesa",     mesa);
        request.setAttribute("fecha",    fecha);
        request.setAttribute("hora",     hora);
        request.setAttribute("personas", personas);
        request.setAttribute("ocasion",  ocasion);

        request.getRequestDispatcher("/html/m-formulario-reserva.jsp")
               .forward(request, response);
    }
}