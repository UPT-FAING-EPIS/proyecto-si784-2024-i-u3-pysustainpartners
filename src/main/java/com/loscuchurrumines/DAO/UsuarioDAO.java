package com.loscuchurrumines.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.loscuchurrumines.Config.NeonConnection;
import com.loscuchurrumines.Model.Usuario;

public class UsuarioDAO {
    public void cambiarContrasena(String email, String password){
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        String query = "UPDATE tbusuario SET password = ? WHERE email = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, password);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
   
        }

    }

    public boolean validarCodigo(String codigo, String email){
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT * FROM tbusuario WHERE email = ? AND codigo = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, codigo);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
               return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    public Usuario authenticate(String user, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = NeonConnection.getConnection();
            String query = "SELECT * FROM tbusuario WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUser(resultSet.getInt("iduser"));
                usuario.setUser(resultSet.getString("username"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setEstado(resultSet.getBoolean("estado"));
                usuario.setFkCargo(resultSet.getInt("fkcargo"));
                return usuario;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public boolean actualizarRolUsuario(int userId,int newRole){
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        String query = "UPDATE tbusuario SET fkcargo = ? WHERE iduser = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, newRole);
            statement.setInt(2, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public Usuario obtenerUsuario(int idUser) {
        Usuario usuario = new Usuario();
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM tbusuario WHERE iduser = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, idUser);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                usuario.setIdUser(resultSet.getInt("iduser"));
                usuario.setUser(resultSet.getString("username"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setEstado(resultSet.getBoolean("estado"));
                usuario.setFkCargo(resultSet.getInt("fkcargo"));
                return usuario;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT * FROM tbusuario";
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUser(resultSet.getInt("iduser"));
                usuario.setUser(resultSet.getString("username"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setEstado(resultSet.getBoolean("estado"));
                usuario.setFkCargo(resultSet.getInt("fkcargo"));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean crearUsuario(Usuario usuario) {
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        String query = "INSERT INTO tbusuario (username, password, email, estado, fkcargo) VALUES (?,?,?,?,?)";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, usuario.getUser());
            statement.setString(2, usuario.passwod());
            statement.setString(3, usuario.getEmail());
            statement.setBoolean(4, true);
            statement.setInt(5, 1);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usuario) {
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        String query = "UPDATE tbusuario SET username = ?, password = ?, email = ?, estado = ?, fkcargo = ? WHERE iduser = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, usuario.getUser());
            statement.setString(2, usuario.passwod());
            statement.setString(3, usuario.getEmail());
            statement.setBoolean(4, usuario.getEstado());
            statement.setInt(5, usuario.getFkCargo());
            statement.setInt(6, usuario.getIdUser());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Usuario> searchUsuarios(String searchTerm) {
        List<Usuario> usuarios = new ArrayList<>();
        Connection connection = NeonConnection.getConnection();
        String searchWithWildcards = "%" + searchTerm + "%";
        String sql = "SELECT * FROM tbusuario WHERE username LIKE ? and fkcargo = 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, searchWithWildcards);
            

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Usuario usuario = new Usuario();

                usuario.setIdUser(resultSet.getInt("iduser"));
                usuario.setUser(resultSet.getString("username"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setEstado(resultSet.getBoolean("estado"));
                usuario.setFkCargo(resultSet.getInt("fkcargo"));
                usuarios.add(usuario);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return usuarios;
    }
}
