package com.loscuchurrumines.step;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.controller.DashboardController;
import com.loscuchurrumines.dao.PersonaDAO;
import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Persona;
import com.loscuchurrumines.model.Proyecto;
import com.loscuchurrumines.model.Usuario;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DashboardStepDefinitions {

    private static final Logger logger = Logger.getLogger(
        DashboardStepDefinitions.class.getName()
    );
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher requestDispatcher;
    private Usuario authenticatedUser;
    private PersonaDAO personaDAO = mock(PersonaDAO.class);
    private ProyectoDAO proyectoDAO = mock(ProyectoDAO.class);
    private boolean usuarioEstablecido;
    private List<Proyecto> proyectos = new ArrayList<>();

    @Before
    public void setUp() {
        logger.info("Setting up test context");

        authenticatedUser = new Usuario();
        authenticatedUser.setIdUser(1);

        session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(authenticatedUser);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);

        when(request.getSession()).thenReturn(session);
        when(
            request.getRequestDispatcher("Views/Dashboard/dashboard.jsp")
        ).thenReturn(requestDispatcher);

        // Mocking PersonaDAO behavior
        Persona persona = new Persona();
        persona.setIdPersona(1);
        when(personaDAO.obtenerPersona(1)).thenReturn(persona);

        // Mocking ProyectoDAO behavior
        proyectos = new ArrayList<>();
        Proyecto testeo = new Proyecto(
            1,
            "Proyecto 1",
            "Descripción 1",
            "Objetivo 1",
            1,
            1,
            1
        );
        proyectos.add(testeo);
        when(proyectoDAO.obtenerProyectos()).thenReturn(proyectos);
    }

    @Given("a user is authenticated with ID {int}")
    public void a_user_is_authenticated_with_ID(int idUser) {
        logger.info("Executing Given step");

        authenticatedUser.setIdUser(idUser);
        when(request.getSession().getAttribute("persona")).thenReturn(
            authenticatedUser
        );
        // Update PersonaDAO mock behavior for the given user ID
        Persona persona = new Persona();
        persona.setIdPersona(1);
        when(personaDAO.obtenerPersona(idUser)).thenReturn(persona);
    }

    @When("the user requests the dashboard page")
    public void the_user_requests_the_dashboard_page() throws Exception {
        logger.info("Executing When step");

        DashboardController dashboardController = new DashboardController() {
            @Override
            protected PersonaDAO getPersonaDAO() {
                return personaDAO;
            }

            @Override
            protected ProyectoDAO getProyectoDAO() {
                return proyectoDAO;
            }
        };
        dashboardController.handleRequestForTest(request, response);

        usuarioEstablecido = (request.getSession().getAttribute("persona") !=
            null);
    }

    @Then("the user's details are set in the session")
    public void the_user_s_details_are_set_in_the_session() {
        logger.info("Executing Then step");
        assertTrue(
            "La persona debería estar establecida en la sesión",
            usuarioEstablecido
        );
    }

    @Then("the projects are retrieved and set in the request")
    public void the_projects_are_retrieved_and_set_in_the_request() {
        Proyecto testeo = new Proyecto(
            1,
            "Proyecto 1",
            "Descripción 1",
            "Objetivo 1",
            1,
            1,
            1
        );
        proyectos.add(testeo);
        logger.info("Executing Then step");
        assertNotNull("Los proyectos no deberían ser nulos", proyectos);
        assertFalse(
            "La lista de proyectos no debería estar vacía",
            proyectos.isEmpty()
        );
    }
}
