package com.loscuchurrumines.step;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import com.loscuchurrumines.dao.ProyectoDAO;
import com.loscuchurrumines.model.Proyecto;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProyectoStepDefinitions {
    private static final Logger logger = Logger.getLogger(ProyectoStepDefinitions.class.getName());

    private Proyecto proyecto;
    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private boolean resultado;
    private List<Integer> modalidades = new ArrayList<>();
    private List<Integer> categorias = new ArrayList<>();
    private int monto;

    @Given("un proyecto con los siguientes datos")
    public void un_proyecto_con_los_siguientes_datos(DataTable dataTable) {
        logger.info("Given: Datos del proyecto");
        proyecto = new Proyecto(); // Asegúrate de asignar la instancia al atributo de clase
        proyecto.setNombre(dataTable.cell(1, 0));
        proyecto.setDescripcion(dataTable.cell(1, 1));
        proyecto.setObjetivo(dataTable.cell(1, 2));
        proyecto.setFoto(dataTable.cell(1, 3));  
        proyecto.setFkRegion(Integer.parseInt(dataTable.cell(1, 4)));
        proyecto.setFkUser(Integer.parseInt(dataTable.cell(1, 5)));
        monto = Integer.parseInt(dataTable.cell(1, 6));

        // Convertir modalidades y categorías de String a List<Integer>
        String[] modalidadesArray = dataTable.cell(1, 7).split(",");
        for (String modalidad : modalidadesArray) {
            modalidades.add(Integer.parseInt(modalidad.trim()));
        }

        String[] categoriasArray = dataTable.cell(1, 8).split(",");
        for (String categoria : categoriasArray) {
            categorias.add(Integer.parseInt(categoria.trim()));
        }

        logger.info("Datos del proyecto inicializados correctamente: " + proyecto.toString());
    }

    @When("el usuario crea un nuevo proyecto")
    public void el_usuario_crea_un_nuevo_proyecto() throws ServletException, IOException {
        logger.info("When: Intentando crear un nuevo proyecto");
        resultado = proyectoDAO.crearProyecto(proyecto, monto, modalidades, categorias);
        logger.info("Resultado de la creación del proyecto: " + resultado);
    }

    @Then("el proyecto debe ser creado exitosamente")
    public void el_proyecto_debe_ser_creado_exitosamente() throws IOException {
        logger.info("Then: Verificando si el proyecto fue creado exitosamente");
        assertTrue(resultado, "El proyecto no fue creado exitosamente");
        logger.info("El proyecto fue creado exitosamente");
    }
}