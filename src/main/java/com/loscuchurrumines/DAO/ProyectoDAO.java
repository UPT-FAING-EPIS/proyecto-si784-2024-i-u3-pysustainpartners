package com.loscuchurrumines.dao;

import com.loscuchurrumines.config.NeonConnection;
import com.loscuchurrumines.model.Proyecto;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProyectoDAO {

    private static final String IDPROYECTO = "idproyecto";
    private static final String NOMBRE = "nombre";
    private static final String DESCRIPCION = "descripcion";
    private static final String OBJETIVO = "objetivo";
    private static final String FOTO = "foto";
    private static final String ESTADO = "estado";
    private static final String FKREGION = "fkregion";
    private static final String FKUSER = "fkuser";
    private static final String FKFONDO = "fkfondo";

    private static final Logger LOGGER = Logger.getLogger(
        ProyectoDAO.class.getName()
    );

    public Proyecto obtenerProyecto(int idProyecto) {
        Proyecto proyecto = new Proyecto();

        ResultSet resultSet;
        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto WHERE idproyecto = ?";

        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, idProyecto);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                proyecto.setIdProyecto(resultSet.getInt(IDPROYECTO));
                proyecto.setNombre(resultSet.getString(NOMBRE));
                proyecto.setDescripcion(resultSet.getString(DESCRIPCION));
                proyecto.setObjetivo(resultSet.getString(OBJETIVO));
                proyecto.setFoto(resultSet.getString(FOTO));
                proyecto.setEstado(resultSet.getBoolean(ESTADO));
                proyecto.setFkRegion(resultSet.getInt(FKREGION));
                proyecto.setFkUser(resultSet.getInt(FKUSER));
                proyecto.setFkFondo(resultSet.getInt(FKFONDO));

                return proyecto;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int obtenerParticipacionProyectos(int idUser) {
        String query =
            "SELECT count(*) as proyectosParticipados FROM tbparticipante where fkuser = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, idUser);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("proyectosParticipados");
            }
            return 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return 0;
        }
    }

    public int obtenerMontoProyecto(int idProyecto) {
        String query =
            "SELECT SUM(monto) AS total FROM tbdonacion WHERE fkproyecto = ? AND estado = false";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();
            int total = 0;
            if (resultSet.next()) {
                total = resultSet.getInt("total");
            }
            return total;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return 0;
        }
    }

    public List<Proyecto> seteoProyectos(PreparedStatement statement)
        throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();
        ResultSet resultSet;
        resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Proyecto proyecto = new Proyecto();
            proyecto.setIdProyecto(resultSet.getInt(IDPROYECTO));
            proyecto.setNombre(resultSet.getString(NOMBRE));
            proyecto.setDescripcion(resultSet.getString(DESCRIPCION));
            proyecto.setObjetivo(resultSet.getString(OBJETIVO));
            proyecto.setFoto(resultSet.getString(FOTO));
            proyecto.setEstado(resultSet.getBoolean(ESTADO));
            proyecto.setFkRegion(resultSet.getInt(FKREGION));
            proyecto.setFkUser(resultSet.getInt(FKUSER));
            proyecto.setFkFondo(resultSet.getInt(FKFONDO));
            proyectos.add(proyecto);
        }
        return proyectos;
    }

    public List<Proyecto> obtenerProyectos() {
        List<Proyecto> proyectos = new ArrayList<>();

        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            proyectos = seteoProyectos(statement);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return proyectos;
    }

    public int[] obtenerNumeroDonadoresVoluntarios(int idProyecto) {
        String query =
            "SELECT fkrol, COUNT(*) as cantidad FROM tbparticipante WHERE fkproyecto = ? AND (fkrol = 1 OR fkrol = 2) GROUP BY fkrol";

        int[] resultados = new int[2];
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getInt("fkrol") == 1) {
                    resultados[0] = resultSet.getInt("cantidad");
                } else if (resultSet.getInt("fkrol") == 2) {
                    resultados[1] = resultSet.getInt("cantidad");
                }
            }

            return resultados;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return resultados;
        }
    }

    public boolean crearProyecto(
        Proyecto proyecto,
        int monto,
        List<Integer> modalidades,
        List<Integer> categorias
    ) {
        String query = "Call crearNuevoProyecto(?,?,?,?,?,?,?,?,?)";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, proyecto.getNombre());
            statement.setString(2, proyecto.getDescripcion());
            statement.setString(3, proyecto.getObjetivo());
            statement.setString(4, proyecto.getFoto());
            statement.setInt(5, proyecto.getFkRegion());
            statement.setInt(6, proyecto.getFkUser());
            statement.setInt(7, monto);

            Integer[] modalidadesArray = modalidades.toArray(new Integer[0]);
            Array modalidadesSqlArray = connection.createArrayOf(
                "INTEGER",
                modalidadesArray
            );

            Integer[] categoriasArray = categorias.toArray(new Integer[0]);
            Array categoriasSqlArray = connection.createArrayOf(
                "INTEGER",
                categoriasArray
            );
            statement.setArray(8, modalidadesSqlArray);
            statement.setArray(9, categoriasSqlArray);
            statement.execute();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public List<Proyecto> obtenerProyectosDePersona(int idUser) {
        List<Proyecto> proyectos = new ArrayList<>();

        String query =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto where fkuser = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, idUser);
            proyectos = seteoProyectos(statement);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return proyectos;
    }

    public List<Proyecto> searchProyectos(String searchTerm) {
        List<Proyecto> proyectos = new ArrayList<>();

        String searchWithWildcards = "%" + searchTerm + "%";
        String sql =
            "SELECT idproyecto,nombre,descripcion,objetivo,foto,estado,fkregion,fkuser,fkfondo FROM tbproyecto WHERE nombre LIKE ? OR descripcion LIKE ?";

        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, searchWithWildcards);
            statement.setString(2, searchWithWildcards);

            proyectos = seteoProyectos(statement);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }

        return proyectos;
    }

    public boolean cambiarEstadoProyecto(int idProyecto, boolean nuevoEstado) {
        boolean updateSuccess = false;
        String sql = "UPDATE tbproyecto SET estado = ? WHERE idproyecto = ?";
        try (
            Connection conn = NeonConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setBoolean(1, nuevoEstado);
            pstmt.setInt(2, idProyecto);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                updateSuccess = true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }

        return updateSuccess;
    }

    public List<Integer> obtenerCategoriasProyecto(int idProyecto) {
        String query =
            "SELECT fkcategoria FROM tbproyecto_categoria WHERE fkproyecto = ?";
        List<Integer> resultados = new ArrayList<>();
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultados.add(resultSet.getInt("fkcategoria"));
            }

            return resultados;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return resultados;
        }
    }

    public List<Integer> obtenerModalidadesProyecto(int idProyecto) {
        List<Integer> resultados = new ArrayList<>();
        String query =
            "SELECT fkmodalidad FROM tbmodalidad_proyecto WHERE fkproyecto = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultados.add(resultSet.getInt("fkmodalidad"));
            }
            return resultados;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return resultados;
        }
    }

    public List<Map<String, Object>> obtenerProyectosConMetasCumplidas() {
        List<Map<String, Object>> proyectosConMetasCumplidas =
            new ArrayList<>();
        ResultSet resultSet;
        String query =
            "SELECT p.idproyecto, p.nombre, p.foto, p.objetivo, p.estado, f.monto AS meta, " +
            "COALESCE(SUM(d.monto), 0) AS total_recaudado " +
            "FROM tbproyecto p " +
            "INNER JOIN tbfondo f ON p.fkfondo = f.idfondo " +
            "LEFT JOIN tbdonacion d ON p.idproyecto = d.fkproyecto " +
            "GROUP BY p.idproyecto, f.monto " +
            "HAVING COALESCE(SUM(d.monto), 0) >= f.monto;";

        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> proyecto = new HashMap<>();
                proyecto.put("idProyecto", resultSet.getInt(IDPROYECTO));
                proyecto.put(NOMBRE, resultSet.getString(NOMBRE));
                proyecto.put(FOTO, resultSet.getString(FOTO));
                proyecto.put(OBJETIVO, resultSet.getString(OBJETIVO));
                proyecto.put(ESTADO, resultSet.getBoolean(ESTADO));
                proyecto.put("meta", resultSet.getDouble("meta"));
                proyecto.put(
                    "totalRecaudado",
                    resultSet.getDouble("total_recaudado")
                );

                proyectosConMetasCumplidas.add(proyecto);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return proyectosConMetasCumplidas;
    }

    public int getFondo(int idProyecto) {
        String sql =
            "SELECT monto FROM tbproyecto INNER JOIN tbfondo on fkfondo = idfondo where idproyecto = ?";
        try (
            Connection connection = NeonConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, idProyecto);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("monto");
            }
            return 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return 0;
        }
    }
}
