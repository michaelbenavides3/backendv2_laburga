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


//basicamente este es el puente o el conector lo que no dice cuando el cjaeor hace click 

@WebServlet("/CajeroControlador")
public class CajeroControlador extends HttpServlet {
    //// cuando el cajero le da clic a una mesa, entra por aqui (doGet)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idMesa = request.getParameter("idMesa");

        // Usamos el DAO que ya tienes para traer los datos
        DetallePedidoDao dao = new DetallePedidoDao();
       
        // pido la lista de lo que se ha consumido en  esa mesa en especifico
        // uso Integer.parseInt porque el id viene como texto y necesito un numero
        java.util.List<DetallePedido> lista = dao.listarDetallesPorMesa(Integer.parseInt(idMesa));
        //// guardo la lista en el request para que el archivo jsp pueda mostrarla
        request.setAttribute("listaDetalles", lista);
        //// mando todo al archivo jsp que se encarga de pintar la pantalla de cobro
        request.getRequestDispatcher("html/c-cajero.jsp").forward(request, response);
    }
}
