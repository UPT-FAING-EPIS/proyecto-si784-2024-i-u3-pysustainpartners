package com.loscuchurrumines.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import com.loscuchurrumines.DAO.ProyectoDAO;
import com.loscuchurrumines.Model.Persona;
import com.loscuchurrumines.Model.Proyecto;
import com.loscuchurrumines.Model.Usuario;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/proyecto")
public class ProyectoController extends HttpServlet {
    private ProyectoDAO proyectoDAO = new ProyectoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String vista = request.getParameter("vista");
        int idProyecto;

        switch (vista) {
            case "detalleProyecto":
                idProyecto = Integer.parseInt(request.getParameter("id"));
                Proyecto proyecto = proyectoDAO.obtenerProyecto(idProyecto);
                if (proyecto != null) {
                    int[] participantes = proyectoDAO.obtenerNumeroDonadoresVoluntarios(idProyecto);
                    List<Integer> categorias = proyectoDAO.obtenerCategoriasProyecto(idProyecto);
                    int montoRecaudado = proyectoDAO.obtenerMontoProyecto(idProyecto);
                    request.getSession().setAttribute("categorias", categorias);
                    request.getSession().setAttribute("proyectoActual", proyecto);
                    request.getSession().setAttribute("donadores", participantes[0]);
                    request.getSession().setAttribute("voluntarios", participantes[1]);
                    request.getSession().setAttribute("montoRecaudado", montoRecaudado);
                }
                request.getRequestDispatcher("Views/Proyecto/detalleProyecto.jsp").forward(request, response);
                break;
            case "formularioProyecto":
                request.getRequestDispatcher("Views/Proyecto/formularioProyecto.jsp").forward(request, response);
                break;
            default:
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("changeStatus".equals(action)) {
            int idProyecto = Integer.parseInt(request.getParameter("idProyecto"));
            boolean newStatus = Boolean.parseBoolean(request.getParameter("newStatus"));
            Usuario usuarioActual = (Usuario) request.getSession().getAttribute("user");

            if (usuarioActual != null && usuarioActual.getFkCargo() == 2) { // Assuming 2 is the admin role ID
                boolean statusChanged = proyectoDAO.cambiarEstadoProyecto(idProyecto, newStatus);
    
                if (statusChanged) {
                    response.sendRedirect("proyecto?vista=detalleProyecto&id=" + idProyecto);
                } else {
                    response.sendRedirect("proyecto?vista=detalleProyecto&id=" + idProyecto);
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No tiene permiso para realizar esta acción.");
            }
        } else {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String objetivo = request.getParameter("objetivo");
            String foto = request.getParameter("foto");
            int region = Integer.parseInt(request.getParameter("region"));
            int monto = Integer.parseInt(request.getParameter("monto"));
            String[] modalidadesParam = request.getParameterValues("modalidades[]");
            String[] categoriasParam = request.getParameterValues("categorias[]");
            List<Integer> categorias = new ArrayList<>();
            List<Integer> modalidades = new ArrayList<>();
            for (String modalidad : modalidadesParam) {
                modalidades.add(Integer.parseInt(modalidad));
            }
            for (String categoria : categoriasParam) {
                categorias.add(Integer.parseInt(categoria));
            }

            int fkUser = ((Persona) request.getSession().getAttribute("persona")).getFkUser();
            Proyecto proyecto = new Proyecto();
            proyecto.setNombre(nombre);
            proyecto.setDescripcion(descripcion);
            proyecto.setObjetivo(objetivo);
            proyecto.setFoto(foto);
            proyecto.setFkRegion(region);
            proyecto.setFkUser(fkUser);

            boolean result = proyectoDAO.crearProyecto(proyecto, monto, modalidades, categorias);
            if (result) {
                response.sendRedirect("dashboard");
            } else {
                response.sendRedirect("proyecto?vista=formularioProyecto");
            }
        }

    }

}
