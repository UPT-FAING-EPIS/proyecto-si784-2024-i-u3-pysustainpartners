// package com.loscuchurrumines.step;

// import static org.junit.Assert.*;

// import com.loscuchurrumines.controller.LoginController;
// import com.loscuchurrumines.dao.UsuarioDAO;
// import com.loscuchurrumines.model.Usuario;
// import io.cucumber.java.en.Given;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;
// import java.util.logging.Logger;
// import javax.servlet.RequestDispatcher;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;
// import org.mockito.Mockito;

// public class LoginStepDefinitions {

//     private static final Logger logger = Logger.getLogger(
//         LoginStepDefinitions.class.getName()
//     );
//     private HttpServletRequest request;
//     private HttpServletResponse response;
//     private HttpSession session;
//     private RequestDispatcher requestDispatcher;
//     private UsuarioDAO usuarioDAO;
//     private LoginController loginController;
//     private boolean captchaValid;
//     private Usuario authenticatedUser;

//     @Given("un usuario con nombre de usuario {string} y contraseña {string}")
//     public void un_usuario_con_nombre_de_usuario_y_contraseña(
//         String username,
//         String password
//     ) {
//         logger.info("Executing Given step");
//         request = Mockito.mock(HttpServletRequest.class);
//         response = Mockito.mock(HttpServletResponse.class);
//         session = Mockito.mock(HttpSession.class);
//         requestDispatcher = Mockito.mock(RequestDispatcher.class);
//         usuarioDAO = Mockito.mock(UsuarioDAO.class);

//         Mockito.when(request.getParameter("user")).thenReturn(username);
//         Mockito.when(request.getParameter("password")).thenReturn(password);
//         Mockito.when(request.getSession()).thenReturn(session);
//         Mockito.when(
//             request.getRequestDispatcher("Views/Login/login.jsp")
//         ).thenReturn(requestDispatcher);

//         authenticatedUser = new Usuario();
//         authenticatedUser.setEstado(true);

//         Mockito.when(usuarioDAO.authenticate(username, password)).thenReturn(
//             authenticatedUser
//         );

//         loginController = new LoginController() {
//             @Override
//             public UsuarioDAO getUsuarioDAO() {
//                 return usuarioDAO;
//             }
//         };
//     }

//     @When("el usuario intenta iniciar sesión con captcha válido")
//     public void el_usuario_intenta_iniciar_sesión_con_captcha_válido() {
//         logger.info("Executing When step");
//         captchaValid = true;
//         try {
//             Mockito.when(
//                 LoginController.verify(Mockito.anyString(), Mockito.anyString())
//             ).thenReturn(captchaValid);
//             loginController.doPost(request, response);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     @When("el usuario intenta iniciar sesión con captcha inválido")
//     public void el_usuario_intenta_iniciar_sesión_con_captcha_inválido() {
//         logger.info("Executing When step");
//         captchaValid = false;
//         try {
//             Mockito.when(
//                 LoginController.verify(Mockito.anyString(), Mockito.anyString())
//             ).thenReturn(captchaValid);
//             loginController.doPost(request, response);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     @Then("el usuario debe ser redirigido al dashboard")
//     public void el_usuario_debe_ser_redirigido_al_dashboard() {
//         logger.info("Executing Then step");
//         try {
//             Mockito.verify(response).sendRedirect("dashboard");
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     @Then("el usuario debe ver un mensaje de error {string}")
//     public void el_usuario_debe_ver_un_mensaje_de_error(String errorMessage) {
//         logger.info("Executing Then step");
//         Mockito.verify(request).setAttribute("error", errorMessage);
//         try {
//             Mockito.verify(requestDispatcher).forward(request, response);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
