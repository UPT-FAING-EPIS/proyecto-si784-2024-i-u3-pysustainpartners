package com.loscuchurrumines.step;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.controller.LogoutController;
import com.loscuchurrumines.model.Usuario;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutStepDefinitions {

    private static final Logger logger = Logger.getLogger(
        LogoutStepDefinitions.class.getName()
    );
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher requestDispatcher;
    private Usuario authenticatedUser;

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
        when(request.getRequestDispatcher("Views/Login/login.jsp")).thenReturn(
            requestDispatcher
        );
    }

    @Given("an authenticated user with ID {int}")
    public void an_authenticated_user_with_ID(int idUser) {
        logger.info("Executing Given step");

        authenticatedUser.setIdUser(idUser);
        when(request.getSession().getAttribute("user")).thenReturn(
            authenticatedUser
        );
    }

    @When("the user requests the logout page")
    public void the_user_requests_the_logout_page() throws Exception {
        logger.info("Executing When step");

        LogoutController logoutController = new LogoutController();
        logoutController.handleRequestForTest(request, response);
    }

    @Then("the session is invalidated")
    public void the_session_is_invalidated() {
        logger.info("Executing Then step for session invalidation");

        verify(session).invalidate();
    }

    @Then("the user is redirected to the login page")
    public void the_user_is_redirected_to_the_login_page() {
        logger.info("Executing Then step for redirection");

        try {
            verify(requestDispatcher).forward(request, response);
        } catch (Exception e) {
            fail("Redirection to login page failed");
        }
    }
}
