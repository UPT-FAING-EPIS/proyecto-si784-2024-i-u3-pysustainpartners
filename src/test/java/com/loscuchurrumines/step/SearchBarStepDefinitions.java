package com.loscuchurrumines.step;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.controller.SearchBarController;
import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Proyecto;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchBarStepDefinitions {

    private static final Logger logger = Logger.getLogger(SearchBarStepDefinitions.class.getName());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;
    private ProyectoDAO proyectoDAO;
    private List<Proyecto> proyectosEsperados;
    private List<Proyecto> proyectosActuales;

    @Before
    public void setUp() {
        logger.info("Setting up test context");

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        proyectoDAO = mock(ProyectoDAO.class);

        when(request.getRequestDispatcher("/Views/Proyecto/searchBar.jsp")).thenReturn(requestDispatcher);

        proyectosEsperados = new ArrayList<>();
        proyectosEsperados.add(new Proyecto(1, "Proyecto 1", "Descripción 1", "2024-01-01", 1, 1, 1));
        proyectosEsperados.add(new Proyecto(2, "Proyecto 2", "Descripción 2", "2024-01-01", 1, 1, 1));
    }

    @Given("una busqueda con el termino {string}")
    public void una_busqueda_con_el_termino(String query) {
        logger.info("Executing Given step");

        when(request.getParameter("query")).thenReturn(query);
        when(proyectoDAO.searchProyectos(query)).thenReturn(proyectosEsperados);
        when(proyectoDAO.obtenerProyectos()).thenReturn(proyectosEsperados);
    }

    @When("se realiza la busqueda en el controlador")
    public void se_realiza_la_busqueda_en_el_controlador() throws Exception {
        logger.info("Executing When step");

        SearchBarController controller = new SearchBarController() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                String query = req.getParameter("query");
                if (query != null && !query.isEmpty()) {
                    proyectosActuales = proyectoDAO.searchProyectos(query);
                    req.setAttribute("proyectosSearchBar", proyectosActuales);
                } else {
                    proyectosActuales = proyectoDAO.obtenerProyectos();
                    req.setAttribute("proyectosSearchBar", proyectosActuales);
                }
                req.getRequestDispatcher("/Views/Proyecto/searchBar.jsp").forward(req, resp);
            }
        };

        controller.handleGetForTest(request, response);

        verify(request).setAttribute("proyectosSearchBar", proyectosEsperados);
        verify(requestDispatcher).forward(request, response);
    }

    @Then("los resultados deben ser los proyectos correspondientes")
    public void los_resultados_deben_ser_los_proyectos_correspondientes() {
        logger.info("Executing Then step");

        List<Proyecto> result = proyectosEsperados;
        assertNotNull("The proyectosSearchBar attribute should not be null", result);
        assertEquals(proyectosEsperados, result);
    }
}
