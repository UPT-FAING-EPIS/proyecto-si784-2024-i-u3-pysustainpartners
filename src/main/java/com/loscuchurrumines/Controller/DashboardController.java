package com.loscuchurrumines.Controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.loscuchurrumines.Model.Persona;
import com.loscuchurrumines.Model.Proyecto;
import com.loscuchurrumines.Model.Usuario;
import com.loscuchurrumines.DAO.ProyectoDAO;
import com.loscuchurrumines.DAO.PersonaDAO;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private PersonaDAO personaDAO = new PersonaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!establecerUsuario(request)) {
            response.sendRedirect("persona");
        } else {
            establecerProyectos(request);
            request.getRequestDispatcher("Views/Dashboard/dashboard.jsp").forward(request, response);
        }

    }

    private boolean establecerUsuario(HttpServletRequest request) {
        Usuario authenticatedUser = (Usuario) request.getSession().getAttribute("user");
        int idUser = (int) authenticatedUser.getIdUser();

        Persona persona = personaDAO.obtenerPersona(idUser);
        if (persona != null) {
            request.getSession().setAttribute("persona", persona);
        } else {

            return false;
        }

        return true;
    }

    private void establecerProyectos(HttpServletRequest request) {
        List<Proyecto> proyectos = proyectoDAO.obtenerProyectos();
        request.setAttribute("proyectos", proyectos);
    }
}
