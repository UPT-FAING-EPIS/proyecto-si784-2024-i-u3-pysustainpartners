// package com.loscuchurrumines.step;

// import static org.junit.Assert.*;
// import static org.mockito.Mockito.*;

// import com.loscuchurrumines.controller.ParticiparController;
// import com.loscuchurrumines.dao.ProyectoDAO;
// import com.loscuchurrumines.model.Persona;
// import io.cucumber.java.Before;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.logging.Logger;
// import javax.servlet.RequestDispatcher;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;

// public class ParticiparStepDefinitions {

//     private static final Logger logger = Logger.getLogger(
//         ParticiparStepDefinitions.class.getName()
//     );
//     private HttpServletRequest request;
//     private HttpServletResponse response;
//     private HttpSession session;
//     private RequestDispatcher requestDispatcher;
//     private ProyectoDAO proyectoDAO;
//     private Persona persona;

//     @Before
//     public void setUp() {
//         logger.info("Setting up test context");

//         persona = new Persona();
//         persona.setIdPersona(1);
//         persona.setFkUser(1);

//         session = mock(HttpSession.class);
//         when(session.getAttribute("persona")).thenReturn(persona);

//         request = mock(HttpServletRequest.class);
//         response = mock(HttpServletResponse.class);
//         requestDispatcher = mock(RequestDispatcher.class);
//         proyectoDAO = mock(ProyectoDAO.class);

//         when(request.getSession()).thenReturn(session);
//         when(
//             request.getRequestDispatcher(
//                 "/Views/Proyecto/formularioParticipar.jsp"
//             )
//         ).thenReturn(requestDispatcher);
//     }

//     @Given("a user is authenticated with persona ID {int}")
//     public void a_user_is_authenticated_with_persona_ID(int idPersona) {
//         logger.info("Executing Given step");

//         persona.setIdPersona(idPersona);
//         when(request.getSession().getAttribute("persona")).thenReturn(persona);
//     }

//     @When("the user requests the participation form for project ID {int}")
//     public void the_user_requests_the_participation_form_for_project_ID(
//         int idProyecto
//     ) throws Exception {
//         logger.info("Executing When step");

//         when(request.getParameter("id")).thenReturn(String.valueOf(idProyecto));

//         List<Integer> modalidades = new ArrayList<>();
//         modalidades.add(1);
//         modalidades.add(2);
//         when(proyectoDAO.obtenerModalidadesProyecto(idProyecto)).thenReturn(
//             modalidades
//         );

//         ParticiparController participarController = new ParticiparController() {
//             @Override
//             protected ProyectoDAO getProyectoDAO() {
//                 return proyectoDAO;
//             }
//         };
//         participarController.handleGetForTest(request, response);

//         verify(session).setAttribute("modalidades", modalidades);
//         verify(requestDispatcher).forward(request, response);
//     }

//     @Then("the project modalities are retrieved and set in the session")
//     public void the_project_modalities_are_retrieved_and_set_in_the_session() {
//         logger.info("Executing Then step for modalities retrieval");

//         List<Integer> modalidades = (List<Integer>) session.getAttribute(
//             "modalidades"
//         );
//         assertNotNull("Modalidades should not be null", modalidades);
//         assertFalse("Modalidades should not be empty", modalidades.isEmpty());
//     }

//     @Then("the participation form is displayed")
//     public void the_participation_form_is_displayed() {
//         logger.info("Executing Then step for form display");

//         try {
//             verify(requestDispatcher).forward(request, response);
//         } catch (Exception e) {
//             fail("Form display failed");
//         }
//     }

//     @Given("the user is on the participation form for project ID {int}")
//     public void the_user_is_on_the_participation_form_for_project_ID(
//         int idProyecto
//     ) throws Exception {
//         logger.info("Executing Given step for participation form access");

//         when(request.getParameter("id")).thenReturn(String.valueOf(idProyecto));
//         List<Integer> modalidades = new ArrayList<>();
//         modalidades.add(1);
//         modalidades.add(2);
//         when(proyectoDAO.obtenerModalidadesProyecto(idProyecto)).thenReturn(
//             modalidades
//         );

//         ParticiparController participarController = new ParticiparController() {
//             @Override
//             protected ProyectoDAO getProyectoDAO() {
//                 return proyectoDAO;
//             }
//         };
//         participarController.handleGetForTest(request, response);

//         verify(session).setAttribute("modalidades", modalidades);
//         verify(requestDispatcher).forward(request, response);
//     }

//     @When("the user submits the participation form with modalidad ID {int}")
//     public void the_user_submits_the_participation_form_with_modalidad_ID(
//         int modalidadID
//     ) throws Exception {
//         logger.info("Executing When step for form submission");

//         when(request.getParameter("id")).thenReturn("1");
//         when(request.getParameter("modalidad")).thenReturn(
//             String.valueOf(modalidadID)
//         );

//         ParticiparController participarController = new ParticiparController();
//         participarController.handlePostForTest(request, response);
//     }

//     @Then(
//         "the user is redirected to the project details page for project ID {int}"
//     )
//     public void the_user_is_redirected_to_the_project_details_page_for_project_ID(
//         int idProyecto
//     ) {
//         logger.info("Executing Then step for redirection");

//         try {
//             verify(response).sendRedirect(
//                 "proyecto?vista=detalleProyecto&id=" + idProyecto
//             );
//         } catch (Exception e) {
//             fail("Redirection to project details page failed");
//         }
//     }
// }
