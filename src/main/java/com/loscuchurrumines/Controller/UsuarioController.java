package com.loscuchurrumines.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loscuchurrumines.DAO.UsuarioDAO;
import com.loscuchurrumines.Model.Usuario;

@WebServlet("/usuario")
public class UsuarioController extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        if (query != null && !query.isEmpty()) {
            List<Usuario> usuarios = usuarioDAO.searchUsuarios(query);
            request.setAttribute("usuariosSearch", usuarios);
        } else {
            List<Usuario> usuarios = usuarioDAO.searchUsuarios("");
            request.setAttribute("usuariosSearch", usuarios);
        }
        request.getRequestDispatcher("/Views/Usuario/listarUsuarios.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("changeRole".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int newRole = Integer.parseInt(request.getParameter("newRole"));

            usuarioDAO.actualizarRolUsuario(userId, newRole);

            response.sendRedirect("usuario");
        }
    }
}
