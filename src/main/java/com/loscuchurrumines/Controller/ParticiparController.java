package com.loscuchurrumines.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loscuchurrumines.DAO.ParticipanteDAO;
import com.loscuchurrumines.DAO.ProyectoDAO;
import com.loscuchurrumines.Model.Participante;
import com.loscuchurrumines.Model.Persona;

@WebServlet("/participar")
public class ParticiparController extends HttpServlet{
    
    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private ParticipanteDAO participanteDAO = new ParticipanteDAO();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int idProyecto = Integer.parseInt(request.getParameter("id"));
        List<Integer> modalidades = proyectoDAO.obtenerModalidadesProyecto(idProyecto);
        request.getSession().setAttribute("modalidades", modalidades);
        request.getRequestDispatcher("/Views/Proyecto/formularioParticipar.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        int fkUser = ((Persona) request.getSession().getAttribute("persona")).getFkUser();
        int idProyecto = Integer.parseInt(request.getParameter("id"));
        int fkRol = Integer.parseInt(request.getParameter("modalidad"));
        int monto = 0;
        if(request.getParameter("monto") ==null){
           monto = 0;
        }else{
           monto = Integer.parseInt(request.getParameter("monto"));
        }
        
        Participante participante = new Participante();
        participante.setFkUser(fkUser);
        participante.setFkRol(fkRol);
        participante.setFkProyecto(idProyecto);

        boolean participacion = participanteDAO.crearParticipante(participante,monto);
        if (participacion) {
            response.sendRedirect("proyecto?vista=detalleProyecto&id=" + idProyecto);
        } else {
           response.sendRedirect("proyecto?vista=detalleProyecto&id=" + idProyecto);
        }
    }
}
