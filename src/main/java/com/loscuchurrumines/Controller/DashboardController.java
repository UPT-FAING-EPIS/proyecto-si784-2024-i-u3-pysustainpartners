package com.loscuchurrumines.controller;

import com.loscuchurrumines.dao.PersonaDAO;
import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Persona;
import com.loscuchurrumines.model.Proyecto;
import com.loscuchurrumines.model.Usuario;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (!establecerUsuario(request)) {
                response.sendRedirect("persona");
            } else {
                establecerProyectos(request);
                request.getRequestDispatcher("Views/Dashboard/dashboard.jsp").forward(request, response);
            }
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, "Exception caught in doGet method", e);
        }
    }

    private boolean establecerUsuario(HttpServletRequest request) {
        PersonaDAO personaDAO = getPersonaDAO();
        Usuario authenticatedUser = (Usuario) request.getSession().getAttribute("user");
        int idUser = authenticatedUser.getIdUser();

        Persona persona = personaDAO.obtenerPersona(idUser);
        if (persona != null) {
            request.getSession().setAttribute("persona", persona);
            return true;
        } else {
            return false;
        }
    }

    private void establecerProyectos(HttpServletRequest request) {
        ProyectoDAO proyectoDAO = getProyectoDAO();
        List<Proyecto> proyectos = proyectoDAO.obtenerProyectos();
        request.setAttribute("proyectos", proyectos);
    }

    protected PersonaDAO getPersonaDAO() {
        return new PersonaDAO();
    }

    protected ProyectoDAO getProyectoDAO() {
        return new ProyectoDAO();
    }

    // Public method for testing purposes
    public void handleRequestForTest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
