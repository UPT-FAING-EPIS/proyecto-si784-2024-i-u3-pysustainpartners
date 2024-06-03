package com.loscuchurrumines.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

    // before set up para abrir conexiones
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(NeonConnection.class);
        when(NeonConnection.getConnection()).thenReturn(mockConnection);
        participanteDAO = new ParticipanteDAO();
    }

    @Test
    public void testCrearParticipanteConFkRol1() throws Exception {
        // arrange es decir preparacion de valores
        Participante participante = new Participante();
        participante.setFkUser(1);
        participante.setFkRol(1);
        participante.setFkProyecto(1);
        int monto = 100;

        when(mockConnection.prepareStatement(anyString())).thenReturn(
            mockStatement
        );
        when(mockStatement.executeUpdate()).thenReturn(1);

        //act es decir ejecucion de la prueba
        boolean result = participanteDAO.crearParticipante(participante, monto);

        //assert es decir verificacion de resultados
        assertTrue(result);
        verify(mockStatement, times(1)).setInt(1, participante.getFkUser());
        verify(mockStatement, times(1)).setInt(2, participante.getFkRol());
        verify(mockStatement, times(1)).setInt(3, participante.getFkProyecto());
        verify(mockStatement, times(1)).setInt(4, monto);
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testCrearParticipanteConFkRolNo1() throws Exception {
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
