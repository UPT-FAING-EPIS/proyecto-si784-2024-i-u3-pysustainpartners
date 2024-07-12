package com.loscuchurrumines.controller;

import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Persona;
import com.loscuchurrumines.model.Proyecto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/perfil")
public class PerfilController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(
        PerfilController.class.getName()
    );

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        try {
            int idUsuario =
                ((Persona) request
                        .getSession()
                        .getAttribute("persona")).getFkUser();
            List<Proyecto> proyectos = proyectoDAO.obtenerProyectosDePersona(
                idUsuario
            );
            int participacionProyectos =
                proyectoDAO.obtenerParticipacionProyectos(idUsuario);
            if (proyectos == null) {
                proyectos = new ArrayList<>();
                request
                    .getSession()
                    .setAttribute("proyectosUsuario", proyectos);
            } else {
                request
                    .getSession()
                    .setAttribute("proyectosUsuario", proyectos);
            }

            request
                .getSession()
                .setAttribute(
                    "cantidadParticipacionProyectos",
                    participacionProyectos
                );

            int cantidadProyectos = proyectos.size();
            request
                .getSession()
                .setAttribute("cantidadProyectos", cantidadProyectos);
            request
                .getRequestDispatcher("/Views/Persona/perfil.jsp")
                .forward(request, response);
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    public void handleGetForTest(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        doGet(request, response);
    }

    protected ProyectoDAO getProyectoDAO() {
        return new ProyectoDAO();
    }
}
