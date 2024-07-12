package com.loscuchurrumines.dao;

import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.model.Participante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticipanteDAO {

    private static final Logger LOGGER = Logger.getLogger(
        ParticipanteDAO.class.getName()
    );

    public boolean crearParticipante(Participante participante, int monto) {
        String query = "call crearDonante(?,?,?,?)";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            if (participante.getFkRol() == 1) {
                statement.setInt(1, participante.getFkUser());
                statement.setInt(2, participante.getFkRol());
                statement.setInt(3, participante.getFkProyecto());
                statement.setInt(4, monto);
                statement.executeUpdate();
                return true;
            } else {
                String query2 =
                    "INSERT INTO tbparticipante (fkuser,fkrol,fkproyecto) VALUES (?,?,?)";
                return insertarMetodo(query2, participante);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    public boolean insertarMetodo(String query, Participante participante) {
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, participante.getFkUser());
            statement.setInt(2, participante.getFkRol());
            statement.setInt(3, participante.getFkProyecto());
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return false;
        }
    }
}
