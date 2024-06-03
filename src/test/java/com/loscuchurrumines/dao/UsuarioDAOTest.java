package com.loscuchurrumines.dao;

import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.model.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        String query = "SELECT email,codigo FROM tbusuario WHERE email = ? AND codigo = ?";

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
        String query = "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE username = ? AND password = ?";

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
        String query = "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE iduser = ?";

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
        String query = "INSERT INTO tbusuario (username, password, email, estado, fkcargo) VALUES (?,?,?,?,?)";

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
        String query = "UPDATE tbusuario SET username = ?, password = ?, email = ?, estado = ?, fkcargo = ? WHERE iduser = ?";

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
}
