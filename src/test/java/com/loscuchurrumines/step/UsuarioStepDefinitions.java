// package com.loscuchurrumines.step;

// import static org.junit.Assert.*;
// import static org.mockito.Mockito.*;

// import com.loscuchurrumines.controller.UsuarioController;
// import com.loscuchurrumines.dao.UsuarioDAO;
// import com.loscuchurrumines.model.Usuario;
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

// public class UsuarioStepDefinitions {

//     private static final Logger logger = Logger.getLogger(
//         UsuarioStepDefinitions.class.getName()
//     );
//     private HttpServletRequest request;
//     private HttpServletResponse response;
//     private RequestDispatcher requestDispatcher;
//     private UsuarioDAO usuarioDAO;
//     private List<Usuario> usuarios;
//     private boolean roleUpdated;

//     @Before
//     public void setUp() {
//         logger.info("Setting up test context");

//         request = mock(HttpServletRequest.class);
//         response = mock(HttpServletResponse.class);
//         requestDispatcher = mock(RequestDispatcher.class);
//         usuarioDAO = mock(UsuarioDAO.class);

//         when(
//             request.getRequestDispatcher("/Views/Usuario/listarUsuarios.jsp")
//         ).thenReturn(requestDispatcher);

//         usuarios = new ArrayList<>();
//         usuarios.add(
//             new Usuario(1, "user1", "email1@example.com", "password1", 1)
//         );
//         usuarios.add(
//             new Usuario(2, "user2", "email2@example.com", "password2", 2)
//         );
//     }

//     @Given("una búsqueda de usuario con el término {string}")
//     public void una_busqueda_de_usuario_con_el_termino(String query) {
//         logger.info("Executing Given step");

//         when(request.getParameter("query")).thenReturn(query);
//         when(usuarioDAO.searchUsuarios(query)).thenReturn(usuarios);
//         when(usuarioDAO.searchUsuarios("")).thenReturn(usuarios);
//     }

//     @When("se realiza la búsqueda de usuario en el controlador")
//     public void se_realiza_la_busqueda_de_usuario_en_el_controlador()
//         throws Exception {
//         logger.info("Executing When step");

//         UsuarioController controller = new UsuarioController() {
//             @Override
//             protected UsuarioDAO getUsuarioDAO() {
//                 return usuarioDAO;
//             }
//         };

//         controller.handleGet(request, response);
//         verify(request).setAttribute("usuariosSearch", usuarios);
//         verify(requestDispatcher).forward(request, response);
//     }

//     @Then("los resultados deben ser los usuarios correspondientes")
//     public void los_resultados_deben_ser_los_usuarios_correspondientes() {
//         logger.info("Executing Then step");

//         List<Usuario> result = (List<Usuario>) request.getAttribute(
//             "usuariosSearch"
//         );
//         assertEquals(usuarios, result);
//     }

//     @Given("una solicitud para cambiar el rol del usuario con ID {int} a {int}")
//     public void una_solicitud_para_cambiar_el_rol_del_usuario_con_ID_a(
//         int userId,
//         int newRole
//     ) {
//         logger.info("Executing Given step");

//         when(request.getParameter("action")).thenReturn("changeRole");
//         when(request.getParameter("userId")).thenReturn(String.valueOf(userId));
//         when(request.getParameter("newRole")).thenReturn(
//             String.valueOf(newRole)
//         );

//         doAnswer(invocation -> {
//             roleUpdated = true;
//             return null;
//         })
//             .when(usuarioDAO)
//             .actualizarRolUsuario(userId, newRole);
//     }

//     @When("se realiza la actualización de rol en el controlador")
//     public void se_realiza_la_actualizacion_de_rol_en_el_controlador()
//         throws Exception {
//         logger.info("Executing When step");

//         UsuarioController controller = new UsuarioController() {
//             @Override
//             protected UsuarioDAO getUsuarioDAO() {
//                 return usuarioDAO;
//             }
//         };

//         controller.handlePost(request, response);
//     }

//     @Then("el rol del usuario debe ser actualizado correctamente")
//     public void el_rol_del_usuario_debe_ser_actualizado_correctamente() {
//         logger.info("Executing Then step");

//         assertTrue(
//             "El rol del usuario debería haber sido actualizado",
//             roleUpdated
//         );
//         verify(response).sendRedirect("usuario");
//     }
// }
