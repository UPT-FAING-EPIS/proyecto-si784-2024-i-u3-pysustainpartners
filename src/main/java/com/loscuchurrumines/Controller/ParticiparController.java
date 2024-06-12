package com.loscuchurrumines.controller;

import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Participante;
import com.loscuchurrumines.model.Persona;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/participar")
public class ParticiparController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(
        ParticiparController.class.getName()
    );

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            ProyectoDAO proyectoDAO = new ProyectoDAO();
            int idProyecto = Integer.parseInt(request.getParameter("id"));
            List<Integer> modalidades = proyectoDAO.obtenerModalidadesProyecto(
                idProyecto
            );
            request.getSession().setAttribute("modalidades", modalidades);
            request
                .getRequestDispatcher(
                    "/Views/Proyecto/formularioParticipar.jsp"
                )
                .forward(request, response);
        } catch (IOException | ServletException | NumberFormatException e) {
            try {
                response.sendRedirect("proyecto");
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
            }
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            int fkUser =
                ((Persona) request
                        .getSession()
                        .getAttribute("persona")).getFkUser();
            int idProyecto = Integer.parseInt(request.getParameter("id"));
            int fkRol = Integer.parseInt(request.getParameter("modalidad"));

            Participante participante = new Participante();
            participante.setFkUser(fkUser);
            participante.setFkRol(fkRol);
            participante.setFkProyecto(idProyecto);

            response.sendRedirect(
                "proyecto?vista=detalleProyecto&id=" + idProyecto
            );
        } catch (IOException | NumberFormatException e) {
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

    // Public method for testing purposes
    public void handlePostForTest(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        doPost(request, response);
    }

    protected ProyectoDAO getProyectoDAO() {
        return new ProyectoDAO();
    }
}
