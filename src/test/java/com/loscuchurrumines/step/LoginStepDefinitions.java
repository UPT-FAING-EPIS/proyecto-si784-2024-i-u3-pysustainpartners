// package com.loscuchurrumines.step;

// import static org.junit.Assert.*;
// import static org.mockito.Mockito.*;

// import com.loscuchurrumines.controller.LoginController;
// import com.loscuchurrumines.dao.UsuarioDAO;
// import com.loscuchurrumines.model.Usuario;
// import io.cucumber.java.Before;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import javax.servlet.RequestDispatcher;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;

// public class LoginStepDefinitions {

//     private HttpServletRequest request;
//     private HttpServletResponse response;
//     private HttpSession session;
//     private RequestDispatcher requestDispatcher;
//     private UsuarioDAO usuarioDAO;
//     private Usuario authenticatedUser;
//     private boolean captchaValid;
//     private boolean userAuthenticated;

//     @Before
//     public void setUp() {
//         request = mock(HttpServletRequest.class);
//         response = mock(HttpServletResponse.class);
//         session = mock(HttpSession.class);
//         requestDispatcher = mock(RequestDispatcher.class);
//         usuarioDAO = mock(UsuarioDAO.class);

//         when(request.getSession()).thenReturn(session);
//         when(request.getRequestDispatcher("Views/Login/login.jsp")).thenReturn(
//             requestDispatcher
//         );
//     }

//     @Given("a user with username {string} and password {string}")
//     public void a_user_with_username_and_password(
//         String username,
//         String password
//     ) {
//         when(request.getParameter("user")).thenReturn(username);
//         when(request.getParameter("password")).thenReturn(password);
//     }

//     @Given("a valid captcha response")
//     public void a_valid_captcha_response() {
//         when(request.getParameter("g-recaptcha-response")).thenReturn(
//             "valid-captcha-response"
//         );
//         captchaValid = true;
//     }

//     @Given("an invalid captcha response")
//     public void an_invalid_captcha_response() {
//         when(request.getParameter("g-recaptcha-response")).thenReturn(
//             "invalid-captcha-response"
//         );
//         captchaValid = false;
//     }

//     @When("the user attempts to log in")
//     public void the_user_attempts_to_log_in() throws Exception {
//         LoginController loginController = new LoginController() {
//             @Override
//             protected UsuarioDAO getUsuarioDAO() {
//                 return usuarioDAO;
//             }

//             @Override
//             public boolean verify(String gRecaptchaResponse, String secretKey) {
//                 return captchaValid;
//             }
//         };

//         authenticatedUser = new Usuario();
//         authenticatedUser.setEstado(true);

//         when(usuarioDAO.authenticate(anyString(), anyString())).thenReturn(
//             authenticatedUser
//         );

//         loginController.handlePostForTest(request, response);

//         userAuthenticated = (request.getSession().getAttribute("user") != null);
//     }

//     @Then("the user is redirected to the dashboard")
//     public void the_user_is_redirected_to_the_dashboard() throws Exception {
//         verify(response).sendRedirect("dashboard");
//         assertTrue("User should be authenticated", userAuthenticated);
//     }

//     @Then("an error message {string} is shown")
//     public void an_error_message_is_shown(String errorMessage)
//         throws Exception {
//         verify(request).setAttribute("error", errorMessage);
//         verify(requestDispatcher).forward(request, response);
//     }
// }
