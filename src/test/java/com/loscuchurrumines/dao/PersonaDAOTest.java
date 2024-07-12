package com.loscuchurrumines.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.model.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@PrepareForTest({ NeonConnection.class })
public class PersonaDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private PersonaDAO personaDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(NeonConnection.class);
        when(NeonConnection.getConnection()).thenReturn(mockConnection);

        personaDAO = new PersonaDAO();

        when(mockConnection.prepareStatement(anyString())).thenReturn(
            mockStatement
        );
    }

    @Test
    public void testPersonaDefaultController() {
        Persona persona = new Persona(
            1,
            "Juan",
            "Perez",
            "951231241",
            "2000-01-01",
            "foto.jpg",
            1
        );

        assertEquals(1, persona.getIdPersona());
        assertEquals("Juan", persona.getNombre());
        assertEquals("Perez", persona.getApellido());
        assertEquals("951231241", persona.getCelular());
        assertEquals("2000-01-01", persona.getFechaNacimiento());
        assertEquals("foto.jpg", persona.getFotoPersona());
        assertEquals(1, persona.getFkUser());
    }

    @Test
    public void testCrearPersonaMenorDeEdad() {
        Persona persona = new Persona();
        persona.setNombre("Juan");
        persona.setApellido("Perez");
        persona.setCelular("123456789");
        persona.setFechaNacimiento("2010-01-01"); // Fecha de nacimiento que hace que la persona sea menor de 18 años
        persona.setSexo("M");
        persona.setFkUser(1);

        boolean result = personaDAO.crearPersona(persona);

        assertFalse(result);
    }

    @Test
    public void testObtenerPersonaDesdeDB() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(
            mockStatement
        );
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("idpersona")).thenReturn(1);
        when(mockResultSet.getString("nombre")).thenReturn("Juan");
        when(mockResultSet.getString("apellido")).thenReturn("Perez");
        when(mockResultSet.getString("celular")).thenReturn("123456789");
        when(mockResultSet.getString("fotopersona")).thenReturn("foto.jpg");
        when(mockResultSet.getString("fechanacimiento")).thenReturn(
            "2000-01-01"
        );
        when(mockResultSet.getString("sexo")).thenReturn("M");
        when(mockResultSet.getInt("fkuser")).thenReturn(1);

        Persona result = personaDAO.obtenerPersona(1);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(mockStatement, times(1)).setInt(1, 1);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerPersonaDesdeDBFalla() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(
            mockStatement
        );
        when(mockStatement.executeQuery()).thenThrow(
            new RuntimeException("Database error")
        );

        Persona result = personaDAO.obtenerPersona(1);

        assertNull(result);
        verify(mockStatement, times(1)).setInt(1, 1);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerPersonas() throws Exception {
        //List<Persona> personas = new ArrayList<>();
        when(mockConnection.prepareStatement(anyString())).thenReturn(
            mockStatement
        );
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("idpersona")).thenReturn(1, 2);
        when(mockResultSet.getString("nombre")).thenReturn("Juan", "Maria");
        when(mockResultSet.getString("apellido")).thenReturn("Perez", "Lopez");
        when(mockResultSet.getString("celular")).thenReturn(
            "123456789",
            "987654321"
        );
        when(mockResultSet.getString("fotopersona")).thenReturn(
            "foto1.jpg",
            "foto2.jpg"
        );
        when(mockResultSet.getString("fechanacimiento")).thenReturn(
            "2000-01-01",
            "1995-05-05"
        );
        when(mockResultSet.getString("sexo")).thenReturn("M", "F");
        when(mockResultSet.getInt("fkuser")).thenReturn(1, 2);

        List<Persona> result = personaDAO.obtenerPersonas(3);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("Maria", result.get(1).getNombre());
        verify(mockStatement, times(1)).setInt(1, 3);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerPersonasFalla() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(
            mockStatement
        );
        when(mockStatement.executeQuery()).thenThrow(
            new RuntimeException("Database error")
        );

        List<Persona> result = personaDAO.obtenerPersonas(3);

        assertTrue(result.isEmpty());
        verify(mockStatement, times(1)).setInt(1, 3);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testCrearPersona() throws Exception {
        Persona persona = new Persona();
        persona.setNombre("Juan");
        persona.setApellido("Perez");
        persona.setCelular("123456789");
        persona.setFechaNacimiento("2000-01-01");
        persona.setSexo("M");
        persona.setFkUser(1);

        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean result = personaDAO.crearPersona(persona);

        assertTrue(result);
        verify(mockStatement, times(1)).setString(1, "Juan");
        verify(mockStatement, times(1)).setString(2, "Perez");
        verify(mockStatement, times(1)).setString(3, "123456789");
        verify(mockStatement, times(1)).setDate(
            eq(4),
            any(java.sql.Date.class)
        );
        verify(mockStatement, times(1)).setString(5, "M");
        verify(mockStatement, times(1)).setInt(6, 1);
        verify(mockStatement, times(1)).setString(
            7,
            "https://img.freepik.com/vector-premium/icono-perfil-usuario-estilo-plano-ilustracion-vector-avatar-miembro-sobre-fondo-aislado-concepto-negocio-signo-permiso-humano_157943-15752.jpg"
        );
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testCrearPersonaFalla() throws Exception {
        Persona persona = new Persona();
        persona.setNombre("Juan");
        persona.setApellido("Perez");
        persona.setCelular("123456789");
        persona.setFechaNacimiento("2000-01-01");
        persona.setSexo("M");
        persona.setFkUser(1);

        when(mockStatement.executeUpdate()).thenThrow(
            new RuntimeException("Database error")
        );

        boolean result = personaDAO.crearPersona(persona);

        assertFalse(result);
        verify(mockStatement, times(1)).setString(1, "Juan");
        verify(mockStatement, times(1)).setString(2, "Perez");
        verify(mockStatement, times(1)).setString(3, "123456789");
        verify(mockStatement, times(1)).setDate(
            eq(4),
            any(java.sql.Date.class)
        );
        verify(mockStatement, times(1)).setString(5, "M");
        verify(mockStatement, times(1)).setInt(6, 1);
        verify(mockStatement, times(1)).setString(
            7,
            "https://img.freepik.com/vector-premium/icono-perfil-usuario-estilo-plano-ilustracion-vector-avatar-miembro-sobre-fondo-aislado-concepto-negocio-signo-permiso-humano_157943-15752.jpg"
        );
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testActualizarPersona() throws Exception {
        Persona persona = new Persona();
        persona.setIdPersona(1);
        persona.setNombre("Juan");
        persona.setApellido("Perez");
        persona.setCelular("123456789");
        persona.setFechaNacimiento("2000-01-01");
        persona.setSexo("M");

        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean result = personaDAO.actualizarPersona(persona);

        assertTrue(result);
        verify(mockStatement, times(1)).setString(1, "Juan");
        verify(mockStatement, times(1)).setString(2, "Perez");
        verify(mockStatement, times(1)).setString(3, "123456789");
        verify(mockStatement, times(1)).setDate(
            eq(4),
            any(java.sql.Date.class)
        );
        verify(mockStatement, times(1)).setString(5, "M");
        verify(mockStatement, times(1)).setInt(6, 1);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testActualizarPersonaFalla() throws Exception {
        Persona persona = new Persona();
        persona.setIdPersona(1);
        persona.setNombre("Juan");
        persona.setApellido("Perez");
        persona.setCelular("123456789");
        persona.setFechaNacimiento("2000-01-01");
        persona.setSexo("M");

        when(mockStatement.executeUpdate()).thenThrow(
            new RuntimeException("Database error")
        );

        boolean result = personaDAO.actualizarPersona(persona);

        assertFalse(result);
        verify(mockStatement, times(1)).setString(1, "Juan");
        verify(mockStatement, times(1)).setString(2, "Perez");
        verify(mockStatement, times(1)).setString(3, "123456789");
        verify(mockStatement, times(1)).setDate(
            eq(4),
            any(java.sql.Date.class)
        );
        verify(mockStatement, times(1)).setString(5, "M");
        verify(mockStatement, times(1)).setInt(6, 1);
        verify(mockStatement, times(1)).executeUpdate();
    }
}
