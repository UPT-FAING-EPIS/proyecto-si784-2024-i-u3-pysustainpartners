package com.loscuchurrumines.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.config.RedisConnection;
import com.loscuchurrumines.model.Proyecto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import redis.clients.jedis.Jedis;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NeonConnection.class, RedisConnection.class})
public class ProyectoDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private Jedis mockJedis;

    private ProyectoDAO proyectoDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(NeonConnection.class);
        PowerMockito.mockStatic(RedisConnection.class);
        when(NeonConnection.getConnection()).thenReturn(mockConnection);
        when(RedisConnection.getConnection()).thenReturn(mockJedis);
        proyectoDAO = new ProyectoDAO();
    }

    @Test
    public void testObtenerProyectoFromRedis() {
        int idProyecto = 1;
        String key = "proyecto:" + idProyecto;
        String cachedProyecto = "{\"idProyecto\":1,\"nombre\":\"Proyecto Test\"}";

        when(mockJedis.exists(key)).thenReturn(true);
        when(mockJedis.get(key)).thenReturn(cachedProyecto);

        Proyecto proyecto = proyectoDAO.obtenerProyecto(idProyecto);

        assertNotNull(proyecto);
        assertEquals(1, proyecto.getIdProyecto());
        assertEquals("Proyecto Test", proyecto.getNombre());
        verify(mockJedis, times(1)).exists(key);
        verify(mockJedis, times(1)).get(key);
    }

    @Test
    public void testObtenerProyectoFromDatabase() throws Exception {
        int idProyecto = 1;
        String query = "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto WHERE idproyecto = ?";
        String key = "proyecto:" + idProyecto;

        when(mockJedis.exists(key)).thenReturn(false);
        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("idproyecto")).thenReturn(1);
        when(mockResultSet.getString("nombre")).thenReturn("Proyecto Test");

        Proyecto proyecto = proyectoDAO.obtenerProyecto(idProyecto);

        assertNotNull(proyecto);
        assertEquals(1, proyecto.getIdProyecto());
        assertEquals("Proyecto Test", proyecto.getNombre());
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
        verify(mockJedis, times(1)).set(eq(key), anyString());
    }

    @Test
    public void testObtenerParticipacionProyectos() throws Exception {
        int idUser = 1;
        String query = "SELECT count(*) as proyectosParticipados FROM tbparticipante where fkuser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("proyectosParticipados")).thenReturn(3);

        int result = proyectoDAO.obtenerParticipacionProyectos(idUser);

        assertEquals(3, result);
        verify(mockStatement, times(1)).setInt(1, idUser);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerMontoProyecto() throws Exception {
        int idProyecto = 1;
        String query = "SELECT SUM(monto) AS total FROM tbdonacion WHERE fkproyecto = ? AND estado = false";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("total")).thenReturn(1000);

        int result = proyectoDAO.obtenerMontoProyecto(idProyecto);

        assertEquals(1000, result);
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testCrearProyecto() throws Exception {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripcion Test");
        proyecto.setObjetivo("Objetivo Test");
        proyecto.setFoto("Foto Test");
        proyecto.setFkRegion(1);
        proyecto.setFkUser(1);
        int monto = 1000;
        List<Integer> modalidades = Arrays.asList(1, 2);
        List<Integer> categorias = Arrays.asList(1, 2);

        String query = "Call crearNuevoProyecto(?,?,?,?,?,?,?,?,?)";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockConnection.createArrayOf(eq("INTEGER"), any(Integer[].class))).thenReturn(mock(Array.class));

        boolean result = proyectoDAO.crearProyecto(proyecto, monto, modalidades, categorias);

        assertTrue(result);
        verify(mockStatement, times(1)).setString(1, proyecto.getNombre());
        verify(mockStatement, times(1)).setString(2, proyecto.getDescripcion());
        verify(mockStatement, times(1)).setString(3, proyecto.getObjetivo());
        verify(mockStatement, times(1)).setString(4, proyecto.getFoto());
        verify(mockStatement, times(1)).setInt(5, proyecto.getFkRegion());
        verify(mockStatement, times(1)).setInt(6, proyecto.getFkUser());
        verify(mockStatement, times(1)).setInt(7, monto);
        verify(mockStatement, times(1)).setArray(eq(8), any(Array.class));
        verify(mockStatement, times(1)).setArray(eq(9), any(Array.class));
        verify(mockStatement, times(1)).execute();
    }
    
    @Test
    public void testObtenerNumeroDonadoresVoluntariosFromRedis() throws Exception {
        int idProyecto = 1;
        String key = "donadoresVoluntarios:" + idProyecto;
        String cachedResultados = "[10, 20]";

        when(mockJedis.exists(key)).thenReturn(true);
        when(mockJedis.get(key)).thenReturn(cachedResultados);

        int[] resultados = proyectoDAO.obtenerNumeroDonadoresVoluntarios(idProyecto);

        assertNotNull(resultados);
        assertEquals(10, resultados[0]);
        assertEquals(20, resultados[1]);
        verify(mockJedis, times(1)).exists(key);
        verify(mockJedis, times(1)).get(key);
    }

    @Test
    public void testObtenerNumeroDonadoresVoluntariosFromDatabase() throws Exception {
        int idProyecto = 1;
        String query = "SELECT fkrol, COUNT(*) as cantidad FROM tbparticipante WHERE fkproyecto = ? AND (fkrol = 1 OR fkrol = 2) GROUP BY fkrol";
        String key = "donadoresVoluntarios:" + idProyecto;

        when(mockJedis.exists(key)).thenReturn(false);
        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("fkrol")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getInt("cantidad")).thenReturn(10).thenReturn(20);

        int[] resultados = proyectoDAO.obtenerNumeroDonadoresVoluntarios(idProyecto);

        assertNotNull(resultados);
        assertEquals(10, resultados[0]);
        assertEquals(20, resultados[1]);
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
        verify(mockJedis, times(1)).set(eq(key), anyString());
    }
}
