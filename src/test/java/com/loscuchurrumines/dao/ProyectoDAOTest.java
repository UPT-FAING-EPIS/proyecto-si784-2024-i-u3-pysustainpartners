package com.loscuchurrumines.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.config.RedisConnection;
import com.loscuchurrumines.model.Proyecto;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import redis.clients.jedis.Jedis;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ NeonConnection.class, RedisConnection.class })
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
        String cachedProyecto =
            "{\"idProyecto\":1,\"nombre\":\"Proyecto Test\"}";

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
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto WHERE idproyecto = ?";
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
        String query =
            "SELECT count(*) as proyectosParticipados FROM tbparticipante where fkuser = ?";

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
        String query =
            "SELECT SUM(monto) AS total FROM tbdonacion WHERE fkproyecto = ? AND estado = false";

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
        when(
            mockConnection.createArrayOf(eq("INTEGER"), any(Integer[].class))
        ).thenReturn(mock(Array.class));

        boolean result = proyectoDAO.crearProyecto(
            proyecto,
            monto,
            modalidades,
            categorias
        );

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
    public void testObtenerNumeroDonadoresVoluntariosFromRedis()
        throws Exception {
        int idProyecto = 1;
        String key = "donadoresVoluntarios:" + idProyecto;
        String cachedResultados = "[10, 20]";

        when(mockJedis.exists(key)).thenReturn(true);
        when(mockJedis.get(key)).thenReturn(cachedResultados);

        int[] resultados = proyectoDAO.obtenerNumeroDonadoresVoluntarios(
            idProyecto
        );

        assertNotNull(resultados);
        assertEquals(10, resultados[0]);
        assertEquals(20, resultados[1]);
        verify(mockJedis, times(1)).exists(key);
        verify(mockJedis, times(1)).get(key);
    }

    @Test
    public void testObtenerNumeroDonadoresVoluntariosFromDatabase()
        throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT fkrol, COUNT(*) as cantidad FROM tbparticipante WHERE fkproyecto = ? AND (fkrol = 1 OR fkrol = 2) GROUP BY fkrol";
        String key = "donadoresVoluntarios:" + idProyecto;

        when(mockJedis.exists(key)).thenReturn(false);
        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("fkrol")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getInt("cantidad")).thenReturn(10).thenReturn(20);

        int[] resultados = proyectoDAO.obtenerNumeroDonadoresVoluntarios(
            idProyecto
        );

        assertNotNull(resultados);
        assertEquals(10, resultados[0]);
        assertEquals(20, resultados[1]);
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
        verify(mockJedis, times(1)).set(eq(key), anyString());
    }

    @Test
    public void testObtenerProyectos() throws Exception {
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("idproyecto")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("nombre"))
            .thenReturn("Proyecto Test 1")
            .thenReturn("Proyecto Test 2");

        List<Proyecto> proyectos = proyectoDAO.obtenerProyectos();

        assertNotNull(proyectos);
        assertEquals(2, proyectos.size());
        assertEquals(1, proyectos.get(0).getIdProyecto());
        assertEquals("Proyecto Test 1", proyectos.get(0).getNombre());
        assertEquals(2, proyectos.get(1).getIdProyecto());
        assertEquals("Proyecto Test 2", proyectos.get(1).getNombre());
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerProyectosDePersona() throws Exception {
        int idUser = 1;
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto where fkuser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("idproyecto")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("nombre"))
            .thenReturn("Proyecto Test 1")
            .thenReturn("Proyecto Test 2");

        List<Proyecto> proyectos = proyectoDAO.obtenerProyectosDePersona(
            idUser
        );

        assertNotNull(proyectos);
        assertEquals(2, proyectos.size());
        assertEquals(1, proyectos.get(0).getIdProyecto());
        assertEquals("Proyecto Test 1", proyectos.get(0).getNombre());
        assertEquals(2, proyectos.get(1).getIdProyecto());
        assertEquals("Proyecto Test 2", proyectos.get(1).getNombre());
        verify(mockStatement, times(1)).setInt(1, idUser);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testSearchProyectos() throws Exception {
        String searchTerm = "Test";
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto WHERE nombre LIKE ? OR descripcion LIKE ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("idproyecto")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("nombre"))
            .thenReturn("Proyecto Test 1")
            .thenReturn("Proyecto Test 2");

        List<Proyecto> proyectos = proyectoDAO.searchProyectos(searchTerm);

        assertNotNull(proyectos);
        assertEquals(2, proyectos.size());
        assertEquals(1, proyectos.get(0).getIdProyecto());
        assertEquals("Proyecto Test 1", proyectos.get(0).getNombre());
        assertEquals(2, proyectos.get(1).getIdProyecto());
        assertEquals("Proyecto Test 2", proyectos.get(1).getNombre());
        verify(mockStatement, times(1)).setString(1, "%" + searchTerm + "%");
        verify(mockStatement, times(1)).setString(2, "%" + searchTerm + "%");
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testCambiarEstadoProyecto() throws Exception {
        int idProyecto = 1;
        boolean nuevoEstado = true;
        String query = "UPDATE tbproyecto SET estado = ? WHERE idproyecto = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean result = proyectoDAO.cambiarEstadoProyecto(
            idProyecto,
            nuevoEstado
        );

        assertTrue(result);
        verify(mockStatement, times(1)).setBoolean(1, nuevoEstado);
        verify(mockStatement, times(1)).setInt(2, idProyecto);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testObtenerCategoriasProyectoFromRedis() throws Exception {
        int idProyecto = 1;
        String key = "categoriasProyecto:" + idProyecto;
        String cachedCategorias = "[1, 2, 3]";

        when(mockJedis.exists(key)).thenReturn(true);
        when(mockJedis.get(key)).thenReturn(cachedCategorias);

        List<Integer> categorias = proyectoDAO.obtenerCategoriasProyecto(
            idProyecto
        );

        assertNotNull(categorias);
        assertEquals(3, categorias.size());
        assertEquals(Integer.valueOf(1), categorias.get(0));
        assertEquals(Integer.valueOf(2), categorias.get(1));
        assertEquals(Integer.valueOf(3), categorias.get(2));
        verify(mockJedis, times(1)).exists(key);
        verify(mockJedis, times(1)).get(key);
    }

    @Test
    public void testObtenerCategoriasProyectoFromDatabase() throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT fkcategoria FROM tbproyecto_categoria WHERE fkproyecto = ?";
        String key = "categoriasProyecto:" + idProyecto;

        when(mockJedis.exists(key)).thenReturn(false);
        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("fkcategoria")).thenReturn(1).thenReturn(2);

        List<Integer> categorias = proyectoDAO.obtenerCategoriasProyecto(
            idProyecto
        );

        assertNotNull(categorias);
        assertEquals(2, categorias.size());
        assertEquals(Integer.valueOf(1), categorias.get(0));
        assertEquals(Integer.valueOf(2), categorias.get(1));
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
        verify(mockJedis, times(1)).set(eq(key), anyString());
    }

    @Test
    public void testObtenerModalidadesProyecto() throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT fkmodalidad FROM tbmodalidad_proyecto WHERE fkproyecto = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("fkmodalidad")).thenReturn(1).thenReturn(2);

        List<Integer> modalidades = proyectoDAO.obtenerModalidadesProyecto(
            idProyecto
        );

        assertNotNull(modalidades);
        assertEquals(2, modalidades.size());
        assertEquals(Integer.valueOf(1), modalidades.get(0));
        assertEquals(Integer.valueOf(2), modalidades.get(1));
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerProyectosConMetasCumplidas() throws Exception {
        String query =
            "SELECT p.idproyecto, p.nombre, p.foto, p.objetivo, p.estado, f.monto AS meta, " +
            "COALESCE(SUM(d.monto), 0) AS total_recaudado " +
            "FROM tbproyecto p " +
            "INNER JOIN tbfondo f ON p.fkfondo = f.idfondo " +
            "LEFT JOIN tbdonacion d ON p.idproyecto = d.fkproyecto " +
            "GROUP BY p.idproyecto, f.monto " +
            "HAVING COALESCE(SUM(d.monto), 0) >= f.monto;";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt("idproyecto")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("nombre"))
            .thenReturn("Proyecto Test 1")
            .thenReturn("Proyecto Test 2");
        when(mockResultSet.getString("foto"))
            .thenReturn("Foto 1")
            .thenReturn("Foto 2");
        when(mockResultSet.getString("objetivo"))
            .thenReturn("Objetivo 1")
            .thenReturn("Objetivo 2");
        when(mockResultSet.getBoolean("estado"))
            .thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getDouble("meta"))
            .thenReturn(1000.0)
            .thenReturn(2000.0);
        when(mockResultSet.getDouble("total_recaudado"))
            .thenReturn(1500.0)
            .thenReturn(2500.0);

        List<Map<String, Object>> proyectos =
            proyectoDAO.obtenerProyectosConMetasCumplidas();

        assertNotNull(proyectos);
        assertEquals(2, proyectos.size());
        assertEquals(1, proyectos.get(0).get("idProyecto"));
        assertEquals("Proyecto Test 1", proyectos.get(0).get("nombre"));
        assertEquals(2, proyectos.get(1).get("idProyecto"));
        assertEquals("Proyecto Test 2", proyectos.get(1).get("nombre"));
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetFondo() throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT monto FROM tbproyecto INNER JOIN tbfondo on fkfondo = idfondo where idproyecto = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("monto")).thenReturn(1000);

        int result = proyectoDAO.getFondo(idProyecto);

        assertEquals(1000, result);
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerProyectoException() throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto WHERE idproyecto = ?";
        String key = "proyecto:" + idProyecto;

        when(mockJedis.exists(key)).thenReturn(false);
        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        Proyecto proyecto = proyectoDAO.obtenerProyecto(idProyecto);

        assertNull(proyecto);
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerParticipacionProyectosException() throws Exception {
        int idUser = 1;
        String query =
            "SELECT count(*) as proyectosParticipados FROM tbparticipante where fkuser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        int result = proyectoDAO.obtenerParticipacionProyectos(idUser);

        assertEquals(0, result);
        verify(mockStatement, times(1)).setInt(1, idUser);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerMontoProyectoException() throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT SUM(monto) AS total FROM tbdonacion WHERE fkproyecto = ? AND estado = false";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        int result = proyectoDAO.obtenerMontoProyecto(idProyecto);

        assertEquals(0, result);
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testCrearProyectoException() throws Exception {
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
        when(
            mockConnection.createArrayOf(eq("INTEGER"), any(Integer[].class))
        ).thenReturn(mock(Array.class));
        doThrow(new SQLException("Database error"))
            .when(mockStatement)
            .execute();

        boolean result = proyectoDAO.crearProyecto(
            proyecto,
            monto,
            modalidades,
            categorias
        );

        assertFalse(result);
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
    public void testObtenerNumeroDonadoresVoluntariosException()
        throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT fkrol, COUNT(*) as cantidad FROM tbparticipante WHERE fkproyecto = ? AND (fkrol = 1 OR fkrol = 2) GROUP BY fkrol";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        int[] resultados = proyectoDAO.obtenerNumeroDonadoresVoluntarios(
            idProyecto
        );

        assertNotNull(resultados);
        assertEquals(0, resultados[0]);
        assertEquals(0, resultados[1]);
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerProyectosException() throws Exception {
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        List<Proyecto> proyectos = proyectoDAO.obtenerProyectos();

        assertNotNull(proyectos);
        assertTrue(proyectos.isEmpty());
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerProyectosDePersonaException() throws Exception {
        int idUser = 1;
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto where fkuser = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        List<Proyecto> proyectos = proyectoDAO.obtenerProyectosDePersona(
            idUser
        );

        assertNotNull(proyectos);
        assertTrue(proyectos.isEmpty());
        verify(mockStatement, times(1)).setInt(1, idUser);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testSearchProyectosException() throws Exception {
        String searchTerm = "Test";
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto WHERE nombre LIKE ? OR descripcion LIKE ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        List<Proyecto> proyectos = proyectoDAO.searchProyectos(searchTerm);

        assertNotNull(proyectos);
        assertTrue(proyectos.isEmpty());
        verify(mockStatement, times(1)).setString(1, "%" + searchTerm + "%");
        verify(mockStatement, times(1)).setString(2, "%" + searchTerm + "%");
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testCambiarEstadoProyectoException() throws Exception {
        int idProyecto = 1;
        boolean nuevoEstado = true;
        String query = "UPDATE tbproyecto SET estado = ? WHERE idproyecto = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(
            new SQLException("Database error")
        );

        boolean result = proyectoDAO.cambiarEstadoProyecto(
            idProyecto,
            nuevoEstado
        );

        assertFalse(result);
        verify(mockStatement, times(1)).setBoolean(1, nuevoEstado);
        verify(mockStatement, times(1)).setInt(2, idProyecto);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testObtenerCategoriasProyectoException() throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT fkcategoria FROM tbproyecto_categoria WHERE fkproyecto = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        List<Integer> categorias = proyectoDAO.obtenerCategoriasProyecto(
            idProyecto
        );

        assertNotNull(categorias);
        assertTrue(categorias.isEmpty());
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerModalidadesProyectoException() throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT fkmodalidad FROM tbmodalidad_proyecto WHERE fkproyecto = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        List<Integer> modalidades = proyectoDAO.obtenerModalidadesProyecto(
            idProyecto
        );

        assertNotNull(modalidades);
        assertTrue(modalidades.isEmpty());
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testObtenerProyectosConMetasCumplidasException()
        throws Exception {
        String query =
            "SELECT p.idproyecto, p.nombre, p.foto, p.objetivo, p.estado, f.monto AS meta, " +
            "COALESCE(SUM(d.monto), 0) AS total_recaudado " +
            "FROM tbproyecto p " +
            "INNER JOIN tbfondo f ON p.fkfondo = f.idfondo " +
            "LEFT JOIN tbdonacion d ON p.idproyecto = d.fkproyecto " +
            "GROUP BY p.idproyecto, f.monto " +
            "HAVING COALESCE(SUM(d.monto), 0) >= f.monto;";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        List<Map<String, Object>> proyectos =
            proyectoDAO.obtenerProyectosConMetasCumplidas();

        assertNotNull(proyectos);
        assertTrue(proyectos.isEmpty());
        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetFondoException() throws Exception {
        int idProyecto = 1;
        String query =
            "SELECT monto FROM tbproyecto INNER JOIN tbfondo on fkfondo = idfondo where idproyecto = ?";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(
            new SQLException("Database error")
        );

        int result = proyectoDAO.getFondo(idProyecto);

        assertEquals(0, result);
        verify(mockStatement, times(1)).setInt(1, idProyecto);
        verify(mockStatement, times(1)).executeQuery();
    }
}
