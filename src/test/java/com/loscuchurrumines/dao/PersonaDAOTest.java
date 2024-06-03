package com.loscuchurrumines.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.config.RedisConnection;
import com.loscuchurrumines.model.Persona;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NeonConnection.class, RedisConnection.class})
public class PersonaDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private Jedis mockJedis;

    private PersonaDAO personaDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(NeonConnection.class);
        PowerMockito.mockStatic(RedisConnection.class);
        when(NeonConnection.getConnection()).thenReturn(mockConnection);
        when(RedisConnection.getConnection()).thenReturn(mockJedis);

        personaDAO = new PersonaDAO();

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
    }

    @Test
    public void testObtenerPersonaDesdeCache() throws Exception {
        Persona persona = new Persona();
        persona.setIdPersona(1);
        persona.setNombre("Juan");
        String personaString = new ObjectMapper().writeValueAsString(persona);

        when(mockJedis.exists("persona:1")).thenReturn(true);
        when(mockJedis.get("persona:1")).thenReturn(personaString);

        Persona result = personaDAO.obtenerPersona(1);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(mockJedis, times(1)).get("persona:1");
    }

    @Test
    public void testObtenerPersonaDesdeDB() throws Exception {
        when(mockJedis.exists("persona:1")).thenReturn(false);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("idpersona")).thenReturn(1);
        when(mockResultSet.getString("nombre")).thenReturn("Juan");
        when(mockResultSet.getString("apellido")).thenReturn("Perez");
        when(mockResultSet.getString("celular")).thenReturn("123456789");
        when(mockResultSet.getString("fotopersona")).thenReturn("foto.jpg");
        when(mockResultSet.getString("fechanacimiento")).thenReturn("2000-01-01");
        when(mockResultSet.getString("sexo")).thenReturn("M");
        when(mockResultSet.getInt("fkuser")).thenReturn(1);

        Persona result = personaDAO.obtenerPersona(1);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(mockStatement, times(1)).setInt(1, 1);
        verify(mockStatement, times(1)).executeQuery();
        verify(mockJedis, times(1)).set(anyString(), anyString());
    }

    @Test
    public void testObtenerPersonas() throws Exception {
        List<Persona> personas = new ArrayList<>();
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("idpersona")).thenReturn(1, 2);
        when(mockResultSet.getString("nombre")).thenReturn("Juan", "Maria");
        when(mockResultSet.getString("apellido")).thenReturn("Perez", "Lopez");
        when(mockResultSet.getString("celular")).thenReturn("123456789", "987654321");
        when(mockResultSet.getString("fotopersona")).thenReturn("foto1.jpg", "foto2.jpg");
        when(mockResultSet.getString("fechanacimiento")).thenReturn("2000-01-01", "1995-05-05");
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
        verify(mockStatement, times(1)).setDate(eq(4), any(java.sql.Date.class));
        verify(mockStatement, times(1)).setString(5, "M");
        verify(mockStatement, times(1)).setInt(6, 1);
        verify(mockStatement, times(1)).setString(7, "https://img.freepik.com/vector-premium/icono-perfil-usuario-estilo-plano-ilustracion-vector-avatar-miembro-sobre-fondo-aislado-concepto-negocio-signo-permiso-humano_157943-15752.jpg");
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
        when(mockJedis.exists("persona:1")).thenReturn(true);

        boolean result = personaDAO.actualizarPersona(persona);

        assertTrue(result);
        verify(mockStatement, times(1)).setString(1, "Juan");
        verify(mockStatement, times(1)).setString(2, "Perez");
        verify(mockStatement, times(1)).setString(3, "123456789");
        verify(mockStatement, times(1)).setDate(eq(4), any(java.sql.Date.class));
        verify(mockStatement, times(1)).setString(5, "M");
        verify(mockStatement, times(1)).setInt(6, 1);
        verify(mockStatement, times(1)).executeUpdate();
        verify(mockJedis, times(1)).del("persona:1");
    }
}

