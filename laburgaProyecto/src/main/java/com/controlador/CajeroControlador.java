package com.controlador;

import com.dao.DetallePedidoDao;
import com.modelo.DetallePedido;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CajeroControlador")
public class CajeroControlador extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idMesa = request.getParameter("idMesa");

        // Usamos el DAO que ya tienes para traer los datos
        DetallePedidoDao dao = new DetallePedidoDao();
        // Nota: Asegúrate de tener un método que busque por ID de mesa
        java.util.List<DetallePedido> lista = dao.listarDetallesPorMesa(Integer.parseInt(idMesa));

        request.setAttribute("listaDetalles", lista);
        request.getRequestDispatcher("html/c-cajero.jsp").forward(request, response);
    }
}
