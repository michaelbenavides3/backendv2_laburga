package com.controlador;

import com.dao.MesaDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CambiarEstadoMesa", urlPatterns = {"/CambiarEstadoMesa"})
public class CambiarEstadoMesa extends HttpServlet {

    // Centralizamos la lógica aquí para que GET y POST hagan lo mismo
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idMesa = request.getParameter("idMesa");
        String nuevoEstado = request.getParameter("estado");

        if (idMesa != null && nuevoEstado != null) {
            MesaDao mesaDao = new MesaDao();
            mesaDao.cambiarEstado(Integer.parseInt(idMesa), nuevoEstado);
        }

        // Regresamos al mesero a su panel
        response.sendRedirect("html/m-meserocopy.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
}