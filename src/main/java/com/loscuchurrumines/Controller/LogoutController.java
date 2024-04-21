package com.loscuchurrumines.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            request.getSession().invalidate();
            request
                .getRequestDispatcher("Views/Login/login.jsp")
                .forward(request, response);
        } catch (IOException | ServletException e) {
            Logger.getLogger(LogoutController.class.getName()).log(
                Level.SEVERE,
                "Exception caught in doGet method",
                e
            );
        }
    }
}
