package com.loscuchurrumines.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.loscuchurrumines.DAO.ProyectoDAO;
import com.loscuchurrumines.Model.Persona;
import com.loscuchurrumines.Model.Proyecto;

@WebServlet("/perfil")
public class PerfilController extends HttpServlet {
    private ProyectoDAO proyectoDAO = new ProyectoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idUsuario = ((Persona) request.getSession().getAttribute("persona")).getFkUser();
        List<Proyecto> proyectos = proyectoDAO.obtenerProyectosDePersona(idUsuario);
        int participacionProyectos = proyectoDAO.obtenerParticipacionProyectos(idUsuario);
        if(proyectos == null){
            proyectos = new ArrayList<>();
            request.getSession().setAttribute("proyectosUsuario", proyectos);
            
        }else{
            request.getSession().setAttribute("proyectosUsuario", proyectos);
            
        }
        
        request.getSession().setAttribute("cantidadParticipacionProyectos", participacionProyectos);
        // Cantidad proyectos
        int cantidadProyectos = proyectos.size();
        request.getSession().setAttribute("cantidadProyectos", cantidadProyectos);
        request.getRequestDispatcher("/Views/Persona/perfil.jsp").forward(request, response);
    }

}
