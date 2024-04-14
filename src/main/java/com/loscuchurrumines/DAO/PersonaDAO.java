package com.loscuchurrumines.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loscuchurrumines.Config.NeonConnection;
import com.loscuchurrumines.Config.RedisConnection;
import com.loscuchurrumines.Model.Persona;

import redis.clients.jedis.Jedis;

public class PersonaDAO {
    private Persona deserializePersona(String personaString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(personaString, Persona.class);
            
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String serializePersona(Persona persona) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(persona);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Persona obtenerPersona(int idPersona) {
        Jedis jedis = RedisConnection.getConnection();
        String key = "persona:" + idPersona;
        if (jedis.exists(key)) {
            String cachedPersona = jedis.get(key);
            return deserializePersona(cachedPersona);
            
        }
        Persona persona = new Persona();
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT * FROM tbpersona WHERE idpersona = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, idPersona);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                persona.setIdPersona(resultSet.getInt("idpersona"));
                persona.setNombre(resultSet.getString("nombre"));
                persona.setApellido(resultSet.getString("apellido"));
                persona.setCelular(resultSet.getString("celular"));
                persona.setFotoPersona(resultSet.getString("fotopersona"));
                persona.setFechaNacimiento(resultSet.getString("fechanacimiento"));
                persona.setSexo(resultSet.getString("sexo"));
                persona.setFkUser(resultSet.getInt("fkuser"));
                jedis.set(key, serializePersona(persona));
                return persona;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Persona> obtenerPersonas(int idPersona) {
        List<Persona> personas = new ArrayList<Persona>();
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT * FROM tbpersona where idpersona !=" + idPersona;
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Persona persona = new Persona();
                persona.setIdPersona(resultSet.getInt("idpersona"));
                persona.setNombre(resultSet.getString("nombre"));
                persona.setApellido(resultSet.getString("apellido"));
                persona.setFotoPersona(resultSet.getString("fotopersona"));
                persona.setCelular(resultSet.getString("celular"));
                persona.setFechaNacimiento(resultSet.getString("fechanacimiento"));
                persona.setSexo(resultSet.getString("sexo"));
                persona.setFkUser(resultSet.getInt("fkuser"));
                personas.add(persona);
            }
            return personas;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean crearPersona(Persona persona){
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        String query = "INSERT INTO tbpersona (nombre, apellido, celular, fechanacimiento, sexo, fkuser, fotopersona) VALUES (?,?,?,?,?,?,?)";
        
        LocalDate date = LocalDate.parse(persona.getFechaNacimiento());
        LocalDate now = LocalDate.now();
        Period period = Period.between(date, now);

        if (period.getYears() < 18) {
            return false;
        }
        try{
            statement = connection.prepareStatement(query);
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setString(3, persona.getCelular());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = format.parse(persona.getFechaNacimiento());
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            statement.setDate(4, sqlDate);
            statement.setString(5, persona.getSexo());
            statement.setInt(6, persona.getFkUser());
            statement.setString(7, "https://img.freepik.com/vector-premium/icono-perfil-usuario-estilo-plano-ilustracion-vector-avatar-miembro-sobre-fondo-aislado-concepto-negocio-signo-permiso-humano_157943-15752.jpg");
            statement.executeUpdate();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        
    }
    public boolean actualizarPersona(Persona persona){
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        Jedis jedis = RedisConnection.getConnection();
        String key = "persona:" + persona.getIdPersona();
        String query = "UPDATE tbpersona SET nombre = ?, apellido = ?, celular = ?, fechanacimiento = ?, sexo = ? WHERE idpersona = ?";
        try{
            statement = connection.prepareStatement(query);
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setString(3, persona.getCelular());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = format.parse(persona.getFechaNacimiento());
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            statement.setDate(4, sqlDate);
            statement.setString(5, persona.getSexo());
            statement.setInt(6, persona.getIdPersona());

            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0){
                if(jedis.exists(key)){
                    jedis.del(key);
                }
            }
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}