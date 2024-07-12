package com.loscuchurrumines.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NeonConnection.class)
public class UsuarioDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private UsuarioDAO usuarioDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(NeonConnection.class);
        when(NeonConnection.getConnection()).thenReturn(mockConnection);
        usuarioDAO = new UsuarioDAO();
    }

    @Test
    public void testDefaultConstructor() {
        Usuario dao = new Usuario(1, "test", "test", "test", true, 1);
        assertEquals(1, dao.getIdUser());
        assertEquals("test", dao.getUser());
        assertEquals("test", dao.passwod());
        assertEquals("test", dao.getEmail());
        assertTrue(dao.getEstado());
        assertEquals(1, dao.getFkCargo());
    } 

    @Test
    public void testCambiarContrasena() throws Exception {
        String email = "test@example.com";
        String password = "newpassword";
        String query = "UPDATE tbusuario SET password = ? WHERE email = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);

        usuarioDAO.cambiarContrasena(email, password);

        verify(mockStatement, times(1)).setString(1, password);
        verify(mockStatement, times(1)).setString(2, email);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testValidarCodigo() throws Exception {
        String email = "test@example.com";
        String codigo = "123456";
        String query =
            "SELECT email,codigo FROM tbusuario WHERE email = ? AND codigo = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean result = usuarioDAO.validarCodigo(codigo, email);

        assertTrue(result);
        verify(mockStatement, times(1)).setString(1, email);
        verify(mockStatement, times(1)).setString(2, codigo);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testAuthenticate() throws Exception {
        String user = "testuser";
        String password = "testpassword";
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE username = ? AND password = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("iduser")).thenReturn(1);
        when(mockResultSet.getString("username")).thenReturn(user);
        when(mockResultSet.getString("password")).thenReturn(password);
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        when(mockResultSet.getBoolean("estado")).thenReturn(true);
        when(mockResultSet.getInt("fkcargo")).thenReturn(1);

        Usuario result = usuarioDAO.authenticate(user, password);

        assertNotNull(result);
        assertEquals(1, result.getIdUser());
        assertEquals(user, result.getUser());
        assertEquals(password, result.passwod());
        assertEquals("test@example.com", result.getEmail());
        assertTrue(result.getEstado());
        assertEquals(1, result.getFkCargo());
        verify(mockStatement, times(1)).setString(1, user);
        verify(mockStatement, times(1)).setString(2, password);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testActualizarRolUsuario() throws Exception {
        int userId = 1;
        int newRole = 2;
        String query = "UPDATE tbusuario SET fkcargo = ? WHERE iduser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);

        boolean result = usuarioDAO.actualizarRolUsuario(userId, newRole);

        assertTrue(result);
        verify(mockStatement, times(1)).setInt(1, newRole);
        verify(mockStatement, times(1)).setInt(2, userId);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testObtenerUsuario() throws Exception {
        int idUser = 1;
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE iduser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("iduser")).thenReturn(idUser);
        when(mockResultSet.getString("username")).thenReturn("testuser");
        when(mockResultSet.getString("password")).thenReturn("testpassword");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        when(mockResultSet.getBoolean("estado")).thenReturn(true);
        when(mockResultSet.getInt("fkcargo")).thenReturn(1);

        Usuario result = usuarioDAO.obtenerUsuario(idUser);

        assertNotNull(result);
        assertEquals(idUser, result.getIdUser());
        assertEquals("testuser", result.getUser());
        assertEquals("testpassword", result.passwod());
        assertEquals("test@example.com", result.getEmail());
        assertTrue(result.getEstado());
        assertEquals(1, result.getFkCargo());
        verify(mockStatement, times(1)).setInt(1, idUser);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testCrearUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setPassword("testpassword");
        usuario.setEmail("test@example.com");
        String query =
            "INSERT INTO tbusuario (username, password, email, estado, fkcargo) VALUES (?,?,?,?,?)";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);

        boolean result = usuarioDAO.crearUsuario(usuario);

        assertTrue(result);
        verify(mockStatement, times(1)).setString(1, usuario.getUser());
        verify(mockStatement, times(1)).setString(2, usuario.passwod());
        verify(mockStatement, times(1)).setString(3, usuario.getEmail());
        verify(mockStatement, times(1)).setBoolean(4, true);
        verify(mockStatement, times(1)).setInt(5, 1);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUser(1);
        usuario.setUser("testuser");
        usuario.setPassword("testpassword");
        usuario.setEmail("test@example.com");
        usuario.setEstado(true);
        usuario.setFkCargo(1);
        String query =
            "UPDATE tbusuario SET username = ?, password = ?, email = ?, estado = ?, fkcargo = ? WHERE iduser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);

        boolean result = usuarioDAO.actualizarUsuario(usuario);

        assertTrue(result);
        verify(mockStatement, times(1)).setString(1, usuario.getUser());
        verify(mockStatement, times(1)).setString(2, usuario.passwod());
        verify(mockStatement, times(1)).setString(3, usuario.getEmail());
        verify(mockStatement, times(1)).setBoolean(4, usuario.getEstado());
        verify(mockStatement, times(1)).setInt(5, usuario.getFkCargo());
        verify(mockStatement, times(1)).setInt(6, usuario.getIdUser());
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testObtenerUsuarios() throws Exception {
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);

        when(mockResultSet.getInt("iduser")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("username"))
            .thenReturn("testuser1")
            .thenReturn("testuser2");
        when(mockResultSet.getString("password"))
            .thenReturn("password1")
            .thenReturn("password2");
        when(mockResultSet.getString("email"))
            .thenReturn("test1@example.com")
            .thenReturn("test2@example.com");
        when(mockResultSet.getBoolean("estado"))
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("fkcargo")).thenReturn(1).thenReturn(2);

        List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());

        Usuario usuario1 = usuarios.get(0);
        assertEquals(1, usuario1.getIdUser());
        assertEquals("testuser1", usuario1.getUser());
        assertEquals("password1", usuario1.passwod());
        assertEquals("test1@example.com", usuario1.getEmail());
        assertTrue(usuario1.getEstado());
        assertEquals(1, usuario1.getFkCargo());

        Usuario usuario2 = usuarios.get(1);
        assertEquals(2, usuario2.getIdUser());
        assertEquals("testuser2", usuario2.getUser());
        assertEquals("password2", usuario2.passwod());
        assertEquals("test2@example.com", usuario2.getEmail());
        assertFalse(usuario2.getEstado());
        assertEquals(2, usuario2.getFkCargo());

        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testSearchUsuarios() throws Exception {
        String searchTerm = "test";
        String searchWithWildcards = "%" + searchTerm + "%";
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE username LIKE ? and fkcargo = 1";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
            
        when(mockResultSet.getInt("iduser")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("username"))
            .thenReturn("testuser1")
            .thenReturn("testuser2");
        when(mockResultSet.getString("password"))
            .thenReturn("password1")
            .thenReturn("password2");
        when(mockResultSet.getString("email"))
            .thenReturn("test1@example.com")
            .thenReturn("test2@example.com");
        when(mockResultSet.getBoolean("estado"))
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("fkcargo")).thenReturn(1).thenReturn(1);

        List<Usuario> usuarios = usuarioDAO.searchUsuarios(searchTerm);

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());

        Usuario usuario1 = usuarios.get(0);
        assertEquals(1, usuario1.getIdUser());
        assertEquals("testuser1", usuario1.getUser());
        assertEquals("password1", usuario1.passwod());
        assertEquals("test1@example.com", usuario1.getEmail());
        assertTrue(usuario1.getEstado());
        assertEquals(1, usuario1.getFkCargo());

        Usuario usuario2 = usuarios.get(1);
        assertEquals(2, usuario2.getIdUser());
        assertEquals("testuser2", usuario2.getUser());
        assertEquals("password2", usuario2.passwod());
        assertEquals("test2@example.com", usuario2.getEmail());
        assertFalse(usuario2.getEstado());
        assertEquals(1, usuario2.getFkCargo());

        verify(mockStatement, times(1)).setString(1, searchWithWildcards);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerUsuariosException() throws Exception {
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();

        assertNotNull(usuarios);
        assertTrue(usuarios.isEmpty());
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testSearchUsuariosException() throws Exception {
        String searchTerm = "test";
        String searchWithWildcards = "%" + searchTerm + "%";
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE username LIKE ? and fkcargo = 1";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        List<Usuario> usuarios = usuarioDAO.searchUsuarios(searchTerm);

        assertNotNull(usuarios);
        assertTrue(usuarios.isEmpty());
        verify(mockStatement, times(1)).setString(1, searchWithWildcards);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testCambiarContrasenaException() throws Exception {
        String email = "test@example.com";
        String password = "newpassword";
        String query = "UPDATE tbusuario SET password = ? WHERE email = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        doThrow(new SQLException("Database error"))
            .when(mockStatement)
            .executeUpdate();

        usuarioDAO.cambiarContrasena(email, password);

        verify(mockStatement, times(1)).setString(1, password);
        verify(mockStatement, times(1)).setString(2, email);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testValidarCodigoException() throws Exception {
        String email = "test@example.com";
        String codigo = "123456";
        String query =
            "SELECT email,codigo FROM tbusuario WHERE email = ? AND codigo = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        boolean result = usuarioDAO.validarCodigo(codigo, email);

        assertFalse(result);
        verify(mockStatement, times(1)).setString(1, email);
        verify(mockStatement, times(1)).setString(2, codigo);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testAuthenticateException() throws Exception {
        String user = "testuser";
        String password = "testpassword";
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE username = ? AND password = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        Usuario result = usuarioDAO.authenticate(user, password);

        assertNull(result);
        verify(mockStatement, times(1)).setString(1, user);
        verify(mockStatement, times(1)).setString(2, password);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testActualizarRolUsuarioException() throws Exception {
        int userId = 1;
        int newRole = 2;
        String query = "UPDATE tbusuario SET fkcargo = ? WHERE iduser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        doThrow(new SQLException("Database error"))
            .when(mockStatement)
            .executeUpdate();

        boolean result = usuarioDAO.actualizarRolUsuario(userId, newRole);

        assertFalse(result);
        verify(mockStatement, times(1)).setInt(1, newRole);
        verify(mockStatement, times(1)).setInt(2, userId);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testObtenerUsuarioException() throws Exception {
        int idUser = 1;
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE iduser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        Usuario result = usuarioDAO.obtenerUsuario(idUser);

        assertNull(result);
        verify(mockStatement, times(1)).setInt(1, idUser);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testCrearUsuarioException() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setPassword("testpassword");
        usuario.setEmail("test@example.com");
        String query =
            "INSERT INTO tbusuario (username, password, email, estado, fkcargo) VALUES (?,?,?,?,?)";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        doThrow(new SQLException("Database error"))
            .when(mockStatement)
            .executeUpdate();

        boolean result = usuarioDAO.crearUsuario(usuario);

        assertFalse(result);
        verify(mockStatement, times(1)).setString(1, usuario.getUser());
        verify(mockStatement, times(1)).setString(2, usuario.passwod());
        verify(mockStatement, times(1)).setString(3, usuario.getEmail());
        verify(mockStatement, times(1)).setBoolean(4, true);
        verify(mockStatement, times(1)).setInt(5, 1);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testActualizarUsuarioException() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUser(1);
        usuario.setUser("testuser");
        usuario.setPassword("testpassword");
        usuario.setEmail("test@example.com");
        usuario.setEstado(true);
        usuario.setFkCargo(1);
        String query =
            "UPDATE tbusuario SET username = ?, password = ?, email = ?, estado = ?, fkcargo = ? WHERE iduser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        doThrow(new SQLException("Database error"))
            .when(mockStatement)
            .executeUpdate();

        boolean result = usuarioDAO.actualizarUsuario(usuario);

        assertFalse(result);
        verify(mockStatement, times(1)).setString(1, usuario.getUser());
        verify(mockStatement, times(1)).setString(2, usuario.passwod());
        verify(mockStatement, times(1)).setString(3, usuario.getEmail());
        verify(mockStatement, times(1)).setBoolean(4, usuario.getEstado());
        verify(mockStatement, times(1)).setInt(5, usuario.getFkCargo());
        verify(mockStatement, times(1)).setInt(6, usuario.getIdUser());
        verify(mockStatement, times(1)).executeUpdate();
    }
}
