package com.loscuchurrumines.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.loscuchurrumines.Config.NeonConnection;
import com.loscuchurrumines.Model.Participante;

public class ParticipanteDAO {


    public boolean crearParticipante(Participante participante, int monto) {
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        if (participante.getFkRol() == 1) {
            String query = "call crearDonante(?,?,?,?)";
            try {

                statement = connection.prepareStatement(query);
                statement.setInt(1, participante.getFkUser());
                statement.setInt(2, participante.getFkRol());
                statement.setInt(3, participante.getFkProyecto());
                statement.setInt(4, monto);

                statement.executeUpdate();
                return true;
            } catch (Exception e) {
                System.out.println("Error al crear donante: " + e.getMessage());
                return false;
            }

        } else {
            String query = "INSERT INTO tbparticipante (fkuser,fkrol,fkproyecto) VALUES (?,?,?)";
            try {

                statement = connection.prepareStatement(query);
                statement.setInt(1, participante.getFkUser());
                statement.setInt(2, participante.getFkRol());
                statement.setInt(3, participante.getFkProyecto());

                statement.executeUpdate();
                return true;
            } catch (Exception e) {
                System.out.println("Error al crear participante: " + e.getMessage());
                return false;
            }
        }
    }

}
