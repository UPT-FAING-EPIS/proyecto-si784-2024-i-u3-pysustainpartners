package com.loscuchurrumines.controller;

import com.loscuchurrumines.dao.UsuarioDAO;
import com.loscuchurrumines.model.Usuario;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(
        RegisterController.class.getName()
    );

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            request
                .getRequestDispatcher("Views/Login/register.jsp")
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
            String user = request.getParameter("user");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String password2 = request.getParameter("password2");
            if (password.equals(password2)) {
                Usuario usuario = new Usuario();
                usuario.setUser(user);
                usuario.setPassword(password);
                usuario.setEmail(email);
                usuarioDAO.crearUsuario(usuario);
                request
                    .getRequestDispatcher("Views/Login/login.jsp")
                    .forward(request, response);
            } else {
                request.setAttribute("error", "Las contraseñas no coinciden");
                request
                    .getRequestDispatcher("Views/Login/register.jsp")
                    .forward(request, response);
            }
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}
