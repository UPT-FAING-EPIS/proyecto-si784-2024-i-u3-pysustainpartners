package com.loscuchurrumines.dao;

import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {

    private static final String IDUSER = "iduser";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String ESTADO = "estado";
    private static final String FKCARGO = "fkcargo";

    private static final Logger LOGGER = Logger.getLogger(
        UsuarioDAO.class.getName()
    );

    public void cambiarContrasena(String email, String password) {
        String query = "UPDATE tbusuario SET password = ? WHERE email = ?";

        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, password);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    public boolean validarCodigo(String codigo, String email) {
        String query =
            "SELECT email,codigo FROM tbusuario WHERE email = ? AND codigo = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet resultSet;
            statement.setString(1, email);
            statement.setString(2, codigo);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return false;
    }

    public Usuario authenticate(String user, String password) {
        ResultSet resultSet = null;
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE username = ? AND password = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, user);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Usuario usuario = new Usuario();

                usuario.setIdUser(resultSet.getInt(IDUSER));
                usuario.setUser(resultSet.getString(USERNAME));
                usuario.setPassword(resultSet.getString(PASSWORD));
                usuario.setEmail(resultSet.getString(EMAIL));
                usuario.setEstado(resultSet.getBoolean(ESTADO));
                usuario.setFkCargo(resultSet.getInt(FKCARGO));
                return usuario;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return null;
    }

    public boolean actualizarRolUsuario(int userId, int newRole) {
        String query = "UPDATE tbusuario SET fkcargo = ? WHERE iduser = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, newRole);
            statement.setInt(2, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return false;
    }

    public Usuario obtenerUsuario(int idUser) {
        Usuario usuario = null; // Inicialmente nulo

        ResultSet resultSet = null;
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE iduser = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, idUser);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                usuario = new Usuario();
                usuario.setIdUser(resultSet.getInt(IDUSER));
                usuario.setUser(resultSet.getString(USERNAME));
                usuario.setPassword(resultSet.getString(PASSWORD));
                usuario.setEmail(resultSet.getString(EMAIL));
                usuario.setEstado(resultSet.getBoolean(ESTADO));
                usuario.setFkCargo(resultSet.getInt(FKCARGO));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return usuario;
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        ResultSet resultSet;
        String query =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUser(resultSet.getInt(IDUSER));
                usuario.setUser(resultSet.getString(USERNAME));
                usuario.setPassword(resultSet.getString(PASSWORD));
                usuario.setEmail(resultSet.getString(EMAIL));
                usuario.setEstado(resultSet.getBoolean(ESTADO));
                usuario.setFkCargo(resultSet.getInt(FKCARGO));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return usuarios;
    }

    public boolean crearUsuario(Usuario usuario) {
        String query =
            "INSERT INTO tbusuario (username, password, email, estado, fkcargo) VALUES (?,?,?,?,?)";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, usuario.getUser());
            statement.setString(2, usuario.passwod());
            statement.setString(3, usuario.getEmail());
            statement.setBoolean(4, true);
            statement.setInt(5, 1);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usuario) {
        String query =
            "UPDATE tbusuario SET username = ?, password = ?, email = ?, estado = ?, fkcargo = ? WHERE iduser = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, usuario.getUser());
            statement.setString(2, usuario.passwod());
            statement.setString(3, usuario.getEmail());
            statement.setBoolean(4, usuario.getEstado());
            statement.setInt(5, usuario.getFkCargo());
            statement.setInt(6, usuario.getIdUser());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    public List<Usuario> searchUsuarios(String searchTerm) {
        List<Usuario> usuarios = new ArrayList<>();
        String searchWithWildcards = "%" + searchTerm + "%";
        String sql =
            "SELECT iduser,username,password,email,estado,fkcargo FROM tbusuario WHERE username LIKE ? and fkcargo = 1";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, searchWithWildcards);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Usuario usuario = new Usuario();

                usuario.setIdUser(resultSet.getInt(IDUSER));
                usuario.setUser(resultSet.getString(USERNAME));
                usuario.setPassword(resultSet.getString(PASSWORD));
                usuario.setEmail(resultSet.getString(EMAIL));
                usuario.setEstado(resultSet.getBoolean(ESTADO));
                usuario.setFkCargo(resultSet.getInt(FKCARGO));
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return usuarios;
    }
}
