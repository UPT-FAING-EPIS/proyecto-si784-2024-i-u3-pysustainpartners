package com.loscuchurrumines.step;

import static org.junit.Assert.*;

import com.loscuchurrumines.dao.PersonaDAO;
import com.loscuchurrumines.model.Persona;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.logging.Logger;

public class PersonaStepDefinitions {

    private static final Logger logger = Logger.getLogger(
        PersonaStepDefinitions.class.getName()
    );
    private Persona persona;
    private PersonaDAO personaDAO = new PersonaDAO();
    private boolean resultado;

    @Given("un usuario con los siguientes datos")
    public void un_usuario_con_los_siguientes_datos(
        io.cucumber.datatable.DataTable dataTable
    ) {
        logger.info("Executing Given step");
        persona = new Persona();
        persona.setNombre(dataTable.cell(1, 0));
        persona.setApellido(dataTable.cell(1, 1));
        persona.setCelular(dataTable.cell(1, 2));
        persona.setFechaNacimiento(dataTable.cell(1, 3));
        persona.setSexo(dataTable.cell(1, 4));
        persona.setFkUser(Integer.parseInt(dataTable.cell(1, 5)));
    }

    @When("el usuario crea una nueva persona")
    public void el_usuario_crea_una_nueva_persona() {
        logger.info("Executing When step");
        resultado = personaDAO.crearPersona(persona);
    }

    @Then("la persona debe ser creada exitosamente")
    public void la_persona_debe_ser_creada_exitosamente() {
        logger.info("Executing Then step");
        assertTrue(resultado);
    }
}
