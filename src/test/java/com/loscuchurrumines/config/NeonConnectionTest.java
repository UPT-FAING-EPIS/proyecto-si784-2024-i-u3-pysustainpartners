package com.loscuchurrumines.config;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

public class NeonConnectionTest {

    @Test
    public void testGetConnection_Success() throws Exception {
        Connection connection = NeonConnection.getConnection();

        assertNotNull("La conexión no debería ser nula", connection);
        assertFalse(
            "La conexión no debería estar cerrada",
            connection.isClosed()
        );

        connection.close();
    }

    @Test
    public void testGetConnection_Failure() throws SQLException {
        Connection connection = NeonConnection.getConnection();

        if (connection == null) {
            fail("La conexión no debería ser nula bajo configuración normal.");
        } else {
            assertFalse(
                "La conexión no debería estar cerrada",
                connection.isClosed()
            );
            connection.close();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor() {
        new NeonConnection();
    }
}
