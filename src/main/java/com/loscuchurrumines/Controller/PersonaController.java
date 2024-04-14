package com.loscuchurrumines.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loscuchurrumines.DAO.PersonaDAO;
import com.loscuchurrumines.Model.Persona;

@WebServlet("/persona")
public class PersonaController extends HttpServlet {

    private PersonaDAO personaDAO = new PersonaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.equals("formularioPersona")) {
            request.getRequestDispatcher("Views/Persona/formularioPersona.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("Views/Persona/editarPersona.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            Persona persona = new Persona();

            persona.setNombre(request.getParameter("nombre"));
            persona.setApellido(request.getParameter("apellido"));
            persona.setCelular(request.getParameter("celular"));
            persona.setFechaNacimiento(request.getParameter("fechaNacimiento"));
            persona.setSexo(request.getParameter("sexo"));
            persona.setFkUser(Integer.parseInt(request.getParameter("fkUser")));
            persona.setFotoPersona("");
            if (personaDAO.crearPersona(persona)) {
                request.getSession().setAttribute("persona", personaDAO.obtenerPersona(persona.getFkUser()));
                response.sendRedirect("dashboard");
            } else {
                request.setAttribute("error", "Error al crear la persona");
                request.getRequestDispatcher("persona").forward(request, response);
            }
        } else if ("update".equals(action)) {
           
            int idPersona = Integer.parseInt(request.getParameter("idPersona"));
            Persona persona = personaDAO.obtenerPersona(idPersona);

            if (persona != null) {
                persona.setNombre(request.getParameter("nombre"));
                persona.setApellido(request.getParameter("apellido"));
                persona.setCelular(request.getParameter("celular"));
                persona.setFechaNacimiento(request.getParameter("fechaNacimiento"));
                persona.setSexo(request.getParameter("sexo"));
               
                boolean isUpdated = personaDAO.actualizarPersona(persona);

                if (isUpdated) {
                   
                    response.sendRedirect("dashboard");
                } else {
                   
                    request.setAttribute("error", "Error al actualizar la persona");
                    request.getRequestDispatcher("Views/Persona/editarPersona.jsp").forward(request, response);
                }
            } else {
                
                request.setAttribute("error", "Persona no encontrada");
                request.getRequestDispatcher("Views/Persona/listaPersonas.jsp").forward(request, response);
            }
        } 
    }

}
