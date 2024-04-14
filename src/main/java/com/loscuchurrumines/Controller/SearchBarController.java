package com.loscuchurrumines.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loscuchurrumines.DAO.ProyectoDAO;
import com.loscuchurrumines.Model.Proyecto;

@WebServlet("/searchBar")
public class SearchBarController extends HttpServlet {
    private ProyectoDAO proyectoDAO = new ProyectoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        if (query != null && !query.isEmpty()) {
            List<Proyecto> proyectos = proyectoDAO.searchProyectos(query);
            request.setAttribute("proyectosSearchBar", proyectos);
        } else {
            List<Proyecto> proyectos = proyectoDAO.obtenerProyectos();
            request.setAttribute("proyectosSearchBar", proyectos);
        }
        request.getRequestDispatcher("/Views/Proyecto/searchBar.jsp").forward(request, response);
    }

}
