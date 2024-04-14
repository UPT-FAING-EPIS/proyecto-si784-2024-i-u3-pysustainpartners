package com.loscuchurrumines.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Array;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loscuchurrumines.Config.NeonConnection;
import com.loscuchurrumines.Config.RedisConnection;
import com.loscuchurrumines.Model.Proyecto;

import redis.clients.jedis.Jedis;

public class ProyectoDAO {
    public Proyecto obtenerProyecto(int idProyecto) {
        Jedis jedis = RedisConnection.getConnection();
        String key = "proyecto:" + idProyecto;

        if (jedis.exists(key)) {
            String cachedProyecto = jedis.get(key);
            return deserializeProyecto(cachedProyecto);
        }

        Proyecto proyecto = new Proyecto();
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT * FROM tbproyecto WHERE idproyecto = ?";

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, idProyecto);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                proyecto.setIdProyecto(resultSet.getInt("idproyecto"));
                proyecto.setNombre(resultSet.getString("nombre"));
                proyecto.setDescripcion(resultSet.getString("descripcion"));
                proyecto.setObjetivo(resultSet.getString("objetivo"));
                proyecto.setFoto(resultSet.getString("foto"));
                proyecto.setEstado(resultSet.getBoolean("estado"));
                proyecto.setFkRegion(resultSet.getInt("fkregion"));
                proyecto.setFkUser(resultSet.getInt("fkuser"));
                proyecto.setFkFondo(resultSet.getInt("fkfondo"));

                jedis.set(key, serializeProyecto(proyecto));
                return proyecto;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Proyecto deserializeProyecto(String proyectoString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(proyectoString, Proyecto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String serializeProyecto(Proyecto proyecto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(proyecto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int obtenerParticipacionProyectos(int idUser) {
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        try {
            String query = "SELECT count(*) as proyectosParticipados FROM tbparticipante where fkuser = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idUser);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("proyectosParticipados");
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int obtenerMontoProyecto(int idProyecto) {
        // Jedis jedis = RedisConnection.getConnection();
        // String key = "montoProyecto:" + idProyecto;
    
        // if (jedis.exists(key)) {
        //     return Integer.parseInt(jedis.get(key));
        // }
    
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        try {
            String query = "SELECT SUM(monto) AS total FROM tbdonacion WHERE fkproyecto = ? AND estado = false";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();
            int total = 0;
            if (resultSet.next()) {
                total = resultSet.getInt("total");
                // jedis.set(key, String.valueOf(total));
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } 
    }

    public List<Proyecto> obtenerProyectos() {
        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT * FROM tbproyecto";
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Proyecto proyecto = new Proyecto();
                proyecto.setIdProyecto(resultSet.getInt("idproyecto"));
                proyecto.setNombre(resultSet.getString("nombre"));
                proyecto.setDescripcion(resultSet.getString("descripcion"));
                proyecto.setObjetivo(resultSet.getString("objetivo"));
                proyecto.setFoto(resultSet.getString("foto"));
                proyecto.setEstado(resultSet.getBoolean("estado"));
                proyecto.setFkRegion(resultSet.getInt("fkregion"));
                proyecto.setFkUser(resultSet.getInt("fkuser"));
                proyecto.setFkFondo(resultSet.getInt("fkfondo"));
                proyectos.add(proyecto);
            }
            return proyectos;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public int[] obtenerNumeroDonadoresVoluntarios(int idProyecto) {
        Jedis jedis = RedisConnection.getConnection();
        String key = "donadoresVoluntarios:" + idProyecto;
        ObjectMapper mapper = new ObjectMapper();

        if (jedis.exists(key)) {
            String cachedResultados = jedis.get(key);
            try {

                return mapper.readValue(cachedResultados, int[].class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        int[] resultados = new int[2];
        try {
            String query = "SELECT fkrol, COUNT(*) as cantidad FROM tbparticipante WHERE fkproyecto = ? AND (fkrol = 1 OR fkrol = 2) GROUP BY fkrol";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getInt("fkrol") == 1) {
                    resultados[0] = resultSet.getInt("cantidad");
                } else if (resultSet.getInt("fkrol") == 2) {
                    resultados[1] = resultSet.getInt("cantidad");
                }
            }
            String serializedResultados = mapper.writeValueAsString(resultados);
            jedis.set(key, serializedResultados);

            return resultados;
        } catch (Exception e) {
            e.printStackTrace();
            return resultados;
        }
    }

    public boolean crearProyecto(Proyecto proyecto, int monto, List<Integer> modalidades, List<Integer> categorias) {
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        try {
            String query = "Call crearNuevoProyecto(?,?,?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, proyecto.getNombre());
            statement.setString(2, proyecto.getDescripcion());
            statement.setString(3, proyecto.getObjetivo());
            statement.setString(4, proyecto.getFoto());
            statement.setInt(5, proyecto.getFkRegion());
            statement.setInt(6, proyecto.getFkUser());
            statement.setInt(7, monto);

            Integer[] modalidadesArray = modalidades.toArray(new Integer[0]);
            Array modalidadesSqlArray = connection.createArrayOf("INTEGER", modalidadesArray);

            Integer[] categoriasArray = categorias.toArray(new Integer[0]);
            Array categoriasSqlArray = connection.createArrayOf("INTEGER", categoriasArray);
            statement.setArray(8, modalidadesSqlArray);
            statement.setArray(9, categoriasSqlArray);
            statement.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Proyecto> obtenerProyectosDePersona(int idUser) {
        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        String query = "SELECT * FROM tbproyecto where fkuser = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, idUser);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Proyecto proyecto = new Proyecto();
                proyecto.setIdProyecto(resultSet.getInt("idproyecto"));
                proyecto.setNombre(resultSet.getString("nombre"));
                proyecto.setDescripcion(resultSet.getString("descripcion"));
                proyecto.setObjetivo(resultSet.getString("objetivo"));
                proyecto.setFoto(resultSet.getString("foto"));
                proyecto.setEstado(resultSet.getBoolean("estado"));
                proyecto.setFkRegion(resultSet.getInt("fkregion"));
                proyecto.setFkUser(resultSet.getInt("fkuser"));
                proyecto.setFkFondo(resultSet.getInt("fkfondo"));
                proyectos.add(proyecto);
            }
            return proyectos;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Proyecto> searchProyectos(String searchTerm) {
        List<Proyecto> proyectos = new ArrayList<>();
        Connection connection = NeonConnection.getConnection();
        String searchWithWildcards = "%" + searchTerm + "%";
        String sql = "SELECT * FROM tbproyecto WHERE nombre LIKE ? OR descripcion LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, searchWithWildcards);
            statement.setString(2, searchWithWildcards);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Proyecto proyecto = new Proyecto();

                proyecto.setIdProyecto(resultSet.getInt("idproyecto"));
                proyecto.setNombre(resultSet.getString("nombre"));
                proyecto.setDescripcion(resultSet.getString("descripcion"));
                proyecto.setObjetivo(resultSet.getString("objetivo"));
                proyecto.setFoto(resultSet.getString("foto"));
                proyecto.setEstado(resultSet.getBoolean("estado"));
                proyecto.setFkRegion(resultSet.getInt("fkregion"));
                proyecto.setFkUser(resultSet.getInt("fkuser"));
                proyecto.setFkFondo(resultSet.getInt("fkfondo"));

                proyectos.add(proyecto);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return proyectos;
    }

    public boolean cambiarEstadoProyecto(int idProyecto, boolean nuevoEstado) {
        Connection conn = NeonConnection.getConnection();
        PreparedStatement pstmt = null;
        boolean updateSuccess = false;

        try {
            String sql = "UPDATE tbproyecto SET estado = ? WHERE idproyecto = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, nuevoEstado);
            pstmt.setInt(2, idProyecto);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                updateSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updateSuccess;
    }

    public List<Integer> obtenerCategoriasProyecto(int idProyecto) {
        Jedis jedis = RedisConnection.getConnection();
        String key = "categoriasProyecto:" + idProyecto;
        ObjectMapper mapper = new ObjectMapper();

        if (jedis.exists(key)) {
            String cachedCategorias = jedis.get(key);
            try {
                return mapper.readValue(cachedCategorias, new TypeReference<List<Integer>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        List<Integer> resultados = new ArrayList<Integer>();
        try {
            String query = "SELECT fkcategoria FROM tbproyecto_categoria WHERE fkproyecto = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultados.add(resultSet.getInt("fkcategoria"));
            }

            String serializedResultados = mapper.writeValueAsString(resultados);
            jedis.set(key, serializedResultados);

            return resultados;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> obtenerModalidadesProyecto(int idProyecto) {
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        List<Integer> resultados = new ArrayList<Integer>();
        try {

            String query = "SELECT fkmodalidad FROM tbmodalidad_proyecto WHERE fkproyecto = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultados.add(resultSet.getInt("fkmodalidad"));
            }
            return resultados;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> obtenerProyectosConMetasCumplidas() {

        List<Map<String, Object>> proyectosConMetasCumplidas = new ArrayList<>();
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT p.idproyecto, p.nombre, p.foto, p.objetivo, p.estado, f.monto AS meta, " +
                "COALESCE(SUM(d.monto), 0) AS total_recaudado " +
                "FROM tbproyecto p " +
                "INNER JOIN tbfondo f ON p.fkfondo = f.idfondo " +
                "LEFT JOIN tbdonacion d ON p.idproyecto = d.fkproyecto " +
                "GROUP BY p.idproyecto, f.monto " +
                "HAVING COALESCE(SUM(d.monto), 0) >= f.monto;";

        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> proyecto = new HashMap<>();
                proyecto.put("idProyecto", resultSet.getInt("idproyecto"));
                proyecto.put("nombre", resultSet.getString("nombre"));
                proyecto.put("foto", resultSet.getString("foto"));
                proyecto.put("objetivo", resultSet.getString("objetivo"));
                proyecto.put("estado", resultSet.getBoolean("estado"));
                proyecto.put("meta", resultSet.getDouble("meta"));
                proyecto.put("totalRecaudado", resultSet.getDouble("total_recaudado"));

                proyectosConMetasCumplidas.add(proyecto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return proyectosConMetasCumplidas;
    }

    public int getFondo(int idProyecto) {
        Connection connection = NeonConnection.getConnection();
        PreparedStatement statement;
        String Sql = "SELECT monto FROM tbproyecto INNER JOIN tbfondo on fkfondo = idfondo where idproyecto = ?";
        try {
            statement = connection.prepareStatement(Sql);
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("monto");
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
