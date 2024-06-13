package com.loscuchurrumines.step;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.controller.SearchBarController;
import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Proyecto;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchBarStepDefinitions {

    private static final Logger logger = Logger.getLogger(SearchBarStepDefinitions.class.getName());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;
    private ProyectoDAO proyectoDAO;
    private List<Proyecto> proyectos;

    @Before
    public void setUp() {
        logger.info("Setting up test context");

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        proyectoDAO = mock(ProyectoDAO.class);

        when(request.getRequestDispatcher("/Views/Proyecto/searchBar.jsp")).thenReturn(requestDispatcher);

        proyectos = new ArrayList<>();
        proyectos.add(new Proyecto(1, "Proyecto 1", "Descripción 1", "2024-01-01", 1, 1, 1));
        proyectos.add(new Proyecto(2, "Proyecto 2", "Descripción 2", "2024-01-01", 1, 1, 1));
    }

    @Given("una busqueda con el termino {string}")
    public void una_busqueda_con_el_termino(String query) {
        logger.info("Executing Given step");

        when(request.getParameter("query")).thenReturn(query);
        when(proyectoDAO.searchProyectos(query)).thenReturn(proyectos);
        when(proyectoDAO.obtenerProyectos()).thenReturn(proyectos);
    }

    @When("se realiza la busqueda en el controlador")
    public void se_realiza_la_busqueda_en_el_controlador() throws Exception {
        logger.info("Executing When step");

        SearchBarController controller = new SearchBarController() {
            @Override
            protected ProyectoDAO getProyectoDAO() {
                return proyectoDAO;
            }
        };

        controller.handleGetForTest(request, response);

        // Verificar que se haya establecido correctamente el atributo "proyectosSearchBar" en el request
        Object proyectosAttribute = request.getAttribute("proyectosSearchBar");
        logger.info("Projects set in request attribute (StepDefinitions): " + proyectosAttribute);

        if (proyectosAttribute == null) {
            logger.warning("Attribute proyectosSearchBar is null");
        } else {
            logger.info("Attribute proyectosSearchBar is not null");
        }

        verify(request).setAttribute("proyectosSearchBar", proyectos);
        verify(requestDispatcher).forward(request, response);

        // Asegúrate de que el atributo esté accesible y no sea null
        proyectosAttribute = request.getAttribute("proyectosSearchBar");
        if (proyectosAttribute == null) {
            logger.warning("Attribute proyectosSearchBar is null");
        } else {
            logger.info("Attribute proyectosSearchBar is not null");
        }

        logger.info("Projects set in request attribute: " + request.getAttribute("proyectosSearchBar"));
    }

    @Then("los resultados deben ser los proyectos correspondientes")
    public void los_resultados_deben_ser_los_proyectos_correspondientes() {
        logger.info("Executing Then step");

        // Obtener y verificar el atributo "proyectosSearchBar" del request
        List<Proyecto> result = (List<Proyecto>) request.getAttribute("proyectosSearchBar");
        logger.info("Projects retrieved from request attribute (StepDefinitions): " + result);

        assertNotNull("The proyectosSearchBar attribute should not be null", result);
        assertEquals(proyectos, result);
    }
}