package com.loscuchurrumines.controller;

import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Proyecto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/searchBar")
public class SearchBarController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SearchBarController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ProyectoDAO proyectoDAO = getProyectoDAO();
            String query = request.getParameter("query");
            List<Proyecto> proyectos;
    
            LOGGER.info("Query parameter: " + query);
    
            if (query != null && !query.isEmpty()) {
                proyectos = proyectoDAO.searchProyectos(query);
                LOGGER.info("Query: " + query + ", Projects found: " + proyectos.size());
            } else {
                proyectos = proyectoDAO.obtenerProyectos();
                LOGGER.info("No query, fetching all projects. Projects found: " + proyectos.size());
            }
    
            LOGGER.info("Projects before setting attribute: " + proyectos);

            request.setAttribute("proyectosSearchBar", proyectos);

            Object attributeValue = request.getAttribute("proyectosSearchBar");
            if (attributeValue != null) {
                LOGGER.info("Attribute 'proyectosSearchBar' set successfully with value: " + attributeValue);
            } else {
                LOGGER.warning("Failed to set attribute 'proyectosSearchBar'.");
            }
            
            logRequestAttributes(request); // Asegúrate de pasar el objeto request aquí
            
            // Hacer forward al JSP
            request.getRequestDispatcher("/Views/Proyecto/searchBar.jsp").forward(request, response);
            
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, "Error processing search request", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }    

    private void logRequestAttributes(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        if (attributeNames != null) {
            LOGGER.info("Request attributes:");
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = request.getAttribute(attributeName);
                LOGGER.info("  - " + attributeName + ": " + attributeValue);
            }
        } else {
            LOGGER.warning("No attributes found in request.");
        }
    }
    
    // Public method for testing purposes
    public void handleGetForTest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Executing handleGetForTest() method...");
        doGet(request, response);
    }

    protected ProyectoDAO getProyectoDAO() {
        return new ProyectoDAO();
    }
}