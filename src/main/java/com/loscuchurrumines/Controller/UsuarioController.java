package com.loscuchurrumines.controller;

import com.loscuchurrumines.dao.UsuarioDAO;
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

@WebServlet("/usuario")
public class UsuarioController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(
        UsuarioController.class.getName()
    );

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            String query = request.getParameter("query");
            if (query != null && !query.isEmpty()) {
                List<Usuario> usuarios = usuarioDAO.searchUsuarios(query);
                request.setAttribute("usuariosSearch", usuarios);
            } else {
                List<Usuario> usuarios = usuarioDAO.searchUsuarios("");
                request.setAttribute("usuariosSearch", usuarios);
            }
            request
                .getRequestDispatcher("/Views/Usuario/listarUsuarios.jsp")
                .forward(request, response);
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
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            String action = request.getParameter("action");
            if ("changeRole".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                int newRole = Integer.parseInt(request.getParameter("newRole"));

                usuarioDAO.actualizarRolUsuario(userId, newRole);

                response.sendRedirect("usuario");
            }
        } catch (IOException | NumberFormatException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void handlePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
