package com.loscuchurrumines.controller;

import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Proyecto;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/searchBar")
public class SearchBarController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(
        SearchBarController.class.getName()
    );

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            ProyectoDAO proyectoDAO = new ProyectoDAO();
            String query = request.getParameter("query");
            if (query != null && !query.isEmpty()) {
                List<Proyecto> proyectos = proyectoDAO.searchProyectos(query);
                request.setAttribute("proyectosSearchBar", proyectos);
            } else {
                List<Proyecto> proyectos = proyectoDAO.obtenerProyectos();
                request.setAttribute("proyectosSearchBar", proyectos);
            }
            request
                .getRequestDispatcher("/Views/Proyecto/searchBar.jsp")
                .forward(request, response);
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    // Public method for testing purposes
    public void handleGetForTest(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        doGet(request, response);
    }
}
