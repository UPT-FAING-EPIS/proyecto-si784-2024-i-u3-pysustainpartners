package com.loscuchurrumines.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.model.Participante;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class ParticipanteDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    private ParticipanteDAO participanteDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(NeonConnection.class);
        when(NeonConnection.getConnection()).thenReturn(mockConnection);
        participanteDAO = new ParticipanteDAO();
    }

    @Test
    public void testCrearParticipanteConFkRol1() throws Exception {

        Participante participante = new Participante();
        participante.setFkUser(1);
        participante.setFkRol(1);
        participante.setFkProyecto(1);
        int monto = 100;

        when(mockConnection.prepareStatement(anyString())).thenReturn(
            mockStatement
        );
        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean result = participanteDAO.crearParticipante(participante, monto);

        assertTrue(result);
        verify(mockStatement, times(1)).setInt(1, participante.getFkUser());
        verify(mockStatement, times(1)).setInt(2, participante.getFkRol());
        verify(mockStatement, times(1)).setInt(3, participante.getFkProyecto());
        verify(mockStatement, times(1)).setInt(4, monto);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDefaultConstructor() {
        Participante participante = new Participante();
        assertEquals(0, participante.getIdParticipante());
        assertEquals(0, participante.getFkUser());
        assertEquals(0, participante.getFkRol());
        assertEquals(0, participante.getFkProyecto());
    }

    @Test
    public void testParameterizedConstructor() {
        Participante participante = new Participante(1, 2, 3, 4);
        assertEquals(1, participante.getIdParticipante());
        assertEquals(2, participante.getFkUser());
        assertEquals(3, participante.getFkRol());
        assertEquals(4, participante.getFkProyecto());
    }

    @Test
    public void testSetAndGetIdParticipante() {
        Participante participante = new Participante();
        participante.setIdParticipante(1);
        assertEquals(1, participante.getIdParticipante());
    }

    @Test
    public void testSetAndGetFkUser() {
        Participante participante = new Participante();
        participante.setFkUser(2);
        assertEquals(2, participante.getFkUser());
    }

    @Test
    public void testSetAndGetFkRol() {
        Participante participante = new Participante();
        participante.setFkRol(3);
        assertEquals(3, participante.getFkRol());
    }

    @Test
    public void testSetAndGetFkProyecto() {
        Participante participante = new Participante();
        participante.setFkProyecto(4);
        assertEquals(4, participante.getFkProyecto());
    }

    @Test
    public void testCrearParticipanteConFkRolNo1() {
        Participante participante = new Participante();
        participante.setFkUser(1);
        participante.setFkRol(2);
        participante.setFkProyecto(1);

        ParticipanteDAO spyParticipanteDAO = spy(participanteDAO);
        doReturn(true)
            .when(spyParticipanteDAO)
            .insertarMetodo(anyString(), eq(participante));

        boolean result = spyParticipanteDAO.crearParticipante(
            participante,
            100
        );

        assertTrue(result);
        verify(spyParticipanteDAO, times(1)).insertarMetodo(
            anyString(),
            eq(participante)
        );
    }

    @Test
    public void testCrearParticipanteFalla() throws Exception {
        Participante participante = new Participante();
        participante.setFkUser(1);
        participante.setFkRol(1);
        participante.setFkProyecto(1);
        int monto = 100;

        when(mockConnection.prepareStatement(anyString())).thenReturn(
            mockStatement
        );
        when(mockStatement.executeUpdate()).thenThrow(
            new RuntimeException("Database error")
        );

        boolean result = participanteDAO.crearParticipante(participante, monto);

        assertFalse(result);
        verify(mockStatement, times(1)).setInt(1, participante.getFkUser());
        verify(mockStatement, times(1)).setInt(2, participante.getFkRol());
        verify(mockStatement, times(1)).setInt(3, participante.getFkProyecto());
        verify(mockStatement, times(1)).setInt(4, monto);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testInsertarMetodoExitoso() throws Exception {
        Participante participante = new Participante();
        participante.setFkUser(1);
        participante.setFkRol(2);
        participante.setFkProyecto(1);
        String query =
            "INSERT INTO tbparticipante (fkuser, fkrol, fkproyecto) VALUES (?,?,?)";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean result = participanteDAO.insertarMetodo(query, participante);

        assertTrue(result);
        verify(mockStatement, times(1)).setInt(1, participante.getFkUser());
        verify(mockStatement, times(1)).setInt(2, participante.getFkRol());
        verify(mockStatement, times(1)).setInt(3, participante.getFkProyecto());
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testInsertarMetodoFalla() throws Exception {
        Participante participante = new Participante();
        participante.setFkUser(1);
        participante.setFkRol(2);
        participante.setFkProyecto(1);
        String query =
            "INSERT INTO tbparticipante (fkuser, fkrol, fkproyecto) VALUES (?,?,?)";

        when(mockConnection.prepareStatement(query)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(
            new RuntimeException("Database error")
        );

        boolean result = participanteDAO.insertarMetodo(query, participante);

        assertFalse(result);
        verify(mockStatement, times(1)).setInt(1, participante.getFkUser());
        verify(mockStatement, times(1)).setInt(2, participante.getFkRol());
        verify(mockStatement, times(1)).setInt(3, participante.getFkProyecto());
        verify(mockStatement, times(1)).executeUpdate();
    }
}
