package com.loscuchurrumines.controller;

import com.loscuchurrumines.dao.PersonaDAO;
import com.loscuchurrumines.model.Persona;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/persona")
public class PersonaController extends HttpServlet {

    private static final String ERROR = "error";

    private static final Logger LOGGER = Logger.getLogger(
        PersonaController.class.getName()
    );

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action == null || action.equals("formularioPersona")) {
                request
                    .getRequestDispatcher("Views/Persona/formularioPersona.jsp")
                    .forward(request, response);
            } else {
                request
                    .getRequestDispatcher("Views/Persona/editarPersona.jsp")
                    .forward(request, response);
            }
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            PersonaDAO personaDAO = new PersonaDAO();
            String action = request.getParameter("action");

            if ("create".equals(action)) {
                Persona persona = new Persona();

                persona.setNombre(request.getParameter("nombre"));
                persona.setApellido(request.getParameter("apellido"));
                persona.setCelular(request.getParameter("celular"));
                persona.setFechaNacimiento(
                    request.getParameter("fechaNacimiento")
                );
                persona.setSexo(request.getParameter("sexo"));
                persona.setFkUser(
                    Integer.parseInt(request.getParameter("fkUser"))
                );
                persona.setFotoPersona("");
                if (personaDAO.crearPersona(persona)) {
                    request
                        .getSession()
                        .setAttribute(
                            "persona",
                            personaDAO.obtenerPersona(persona.getFkUser())
                        );
                    response.sendRedirect("dashboard");
                } else {
                    request.setAttribute(ERROR, "Error al crear la persona");
                    request
                        .getRequestDispatcher("persona")
                        .forward(request, response);
                }
            } else if ("update".equals(action)) {
                int idPersona = Integer.parseInt(
                    request.getParameter("idPersona")
                );
                Persona persona = personaDAO.obtenerPersona(idPersona);

                if (persona != null) {
                    persona.setNombre(request.getParameter("nombre"));
                    persona.setApellido(request.getParameter("apellido"));
                    persona.setCelular(request.getParameter("celular"));
                    persona.setFechaNacimiento(
                        request.getParameter("fechaNacimiento")
                    );
                    persona.setSexo(request.getParameter("sexo"));

                    boolean isUpdated = personaDAO.actualizarPersona(persona);

                    if (isUpdated) {
                        response.sendRedirect("dashboard");
                    } else {
                        request.setAttribute(
                            ERROR,
                            "Error al actualizar la persona"
                        );
                        request
                            .getRequestDispatcher(
                                "Views/Persona/editarPersona.jsp"
                            )
                            .forward(request, response);
                    }
                } else {
                    request.setAttribute(ERROR, "Persona no encontrada");
                    request
                        .getRequestDispatcher("Views/Persona/listaPersonas.jsp")
                        .forward(request, response);
                }
            }
        } catch (IOException | ServletException | NumberFormatException e) {
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

    protected PersonaDAO getPersonaDAO() {
        return new PersonaDAO();
    }
}
