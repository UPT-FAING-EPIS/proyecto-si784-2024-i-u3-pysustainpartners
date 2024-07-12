package com.loscuchurrumines.dao;


import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.model.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonaDAO {

    private static final Logger LOGGER = Logger.getLogger(
        PersonaDAO.class.getName()
    );

    public Persona obtenerPersona(int idPersona) {
        Persona persona = new Persona();
        ResultSet resultSet;
        String query =
            "SELECT idpersona, nombre, apellido, celular, fotopersona, fechanacimiento, sexo, fkuser FROM tbpersona WHERE idpersona = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, idPersona);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                persona.setIdPersona(resultSet.getInt("idpersona"));
                persona.setNombre(resultSet.getString("nombre"));
                persona.setApellido(resultSet.getString("apellido"));
                persona.setCelular(resultSet.getString("celular"));
                persona.setFotoPersona(resultSet.getString("fotopersona"));
                persona.setFechaNacimiento(
                    resultSet.getString("fechanacimiento")
                );
                persona.setSexo(resultSet.getString("sexo"));
                persona.setFkUser(resultSet.getInt("fkuser"));
                return persona;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return null;
    }

    public List<Persona> obtenerPersonas(int idPersona) {
        List<Persona> personas = new ArrayList<>();

        ResultSet resultSet;
        String query =
            "SELECT idpersona,nombre,apellido,fotopersona,celular,fechanacimiento,sexo,fkuser FROM tbpersona where idpersona != ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, idPersona);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Persona persona = new Persona();
                persona.setIdPersona(resultSet.getInt("idpersona"));
                persona.setNombre(resultSet.getString("nombre"));
                persona.setApellido(resultSet.getString("apellido"));
                persona.setFotoPersona(resultSet.getString("fotopersona"));
                persona.setCelular(resultSet.getString("celular"));
                persona.setFechaNacimiento(
                    resultSet.getString("fechanacimiento")
                );
                persona.setSexo(resultSet.getString("sexo"));
                persona.setFkUser(resultSet.getInt("fkuser"));
                personas.add(persona);
            }
            return personas;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return personas;
    }

    public boolean crearPersona(Persona persona) {
        String query =
            "INSERT INTO tbpersona (nombre, apellido, celular, fechanacimiento, sexo, fkuser, fotopersona) VALUES (?,?,?,?,?,?,?)";

        LocalDate date = LocalDate.parse(persona.getFechaNacimiento());
        LocalDate now = LocalDate.now();
        Period period = Period.between(date, now);

        if (period.getYears() < 18) {
            return false;
        }
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setString(3, persona.getCelular());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = format.parse(persona.getFechaNacimiento());
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            statement.setDate(4, sqlDate);
            statement.setString(5, persona.getSexo());
            statement.setInt(6, persona.getFkUser());
            statement.setString(
                7,
                "https://img.freepik.com/vector-premium/icono-perfil-usuario-estilo-plano-ilustracion-vector-avatar-miembro-sobre-fondo-aislado-concepto-negocio-signo-permiso-humano_157943-15752.jpg"
            );
            statement.executeUpdate();
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    public boolean actualizarPersona(Persona persona) {
        String query =
            "UPDATE tbpersona SET nombre = ?, apellido = ?, celular = ?, fechanacimiento = ?, sexo = ? WHERE idpersona = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setString(3, persona.getCelular());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = format.parse(persona.getFechaNacimiento());
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            statement.setDate(4, sqlDate);
            statement.setString(5, persona.getSexo());
            statement.setInt(6, persona.getIdPersona());
            statement.executeUpdate();
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return false;
    }
}
