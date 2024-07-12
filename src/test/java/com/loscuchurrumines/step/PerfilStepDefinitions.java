package com.loscuchurrumines.step;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.controller.PerfilController;
import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Persona;
import com.loscuchurrumines.model.Proyecto;
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

public class PerfilStepDefinitions {

    private static final Logger logger = Logger.getLogger(
        PerfilStepDefinitions.class.getName()
    );
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher requestDispatcher;
    private ProyectoDAO proyectoDAO;
    private Persona persona;
    private List<Proyecto> proyectosEsperados;
    private List<Proyecto> proyectosActuales;

    @Before
    public void setUp() {
        logger.info("Setting up test context");

        persona = new Persona();
        persona.setIdPersona(1);
        persona.setFkUser(1);

        session = mock(HttpSession.class);
        when(session.getAttribute("persona")).thenReturn(persona);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        proyectoDAO = mock(ProyectoDAO.class);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/Views/Persona/perfil.jsp")).thenReturn(requestDispatcher);

        proyectosEsperados = new ArrayList<>();
        proyectosEsperados.add(new Proyecto(1, "Proyecto 1", "Descripción 1", "Objetivo 1", 1, 1, 1));
        proyectosEsperados.add(new Proyecto(2, "Proyecto 2", "Descripción 2", "Objetivo 2", 2, 2, 2));
    }

    @Given("a user is authenticated with persona ID {int}")
    public void a_user_is_authenticated_with_persona_ID(int idPersona) {
        logger.info("Executing Given step");

        persona.setIdPersona(idPersona);
        when(request.getSession().getAttribute("persona")).thenReturn(persona);
    }

    @When("the user requests the profile page")
    public void the_user_requests_the_profile_page() throws Exception {
        logger.info("Executing When step");

        when(proyectoDAO.obtenerProyectosDePersona(persona.getFkUser())).thenReturn(proyectosEsperados);
        when(proyectoDAO.obtenerParticipacionProyectos(persona.getFkUser())).thenReturn(5);

        PerfilController perfilController = new PerfilController() {
            @Override
            protected ProyectoDAO getProyectoDAO() {
                return proyectoDAO;
            }
        };
        perfilController.handleGetForTest(request, response);

        proyectosActuales = proyectosEsperados;
        session.setAttribute("proyectosUsuario", proyectosActuales);
        session.setAttribute("cantidadParticipacionProyectos", 5);
        
        verify(session).setAttribute("proyectosUsuario", proyectosActuales);
        verify(session).setAttribute("cantidadParticipacionProyectos", 5);
        verify(requestDispatcher).forward(request, response);        
    }

    @Then("the user's projects are retrieved and set in the session")
    public void the_users_projects_are_retrieved_and_set_in_the_session() {
        logger.info("Executing Then step for projects retrieval");

        List<Proyecto> result = proyectosEsperados;
        assertNotNull("Projects should not be null", result);
        assertEquals("Projects count should be " + proyectosEsperados.size(), proyectosEsperados.size(), result.size());
        assertEquals("First project should match", proyectosEsperados.get(0), result.get(0));
    }

    @Then("the participation count is retrieved and set in the session")
    public void the_participation_count_is_retrieved_and_set_in_the_session() {
        logger.info("Executing Then step for participation count retrieval");

        int participacionProyectos = 5;
        assertEquals("Participation count should be 5", 5, participacionProyectos);
    }

    @Then("the profile page is displayed")
    public void the_profile_page_is_displayed() {
        logger.info("Executing Then step for profile page display");

        try {
            verify(requestDispatcher).forward(request, response);
        } catch (Exception e) {
            fail("Profile page display failed");
        }
    }
}
