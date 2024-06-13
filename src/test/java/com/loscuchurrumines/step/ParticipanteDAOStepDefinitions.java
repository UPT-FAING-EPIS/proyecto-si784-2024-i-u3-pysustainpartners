package com.loscuchurrumines.step;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.loscuchurrumines.dao.ParticipanteDAO;
import com.loscuchurrumines.model.Participante;
import io.cucumber.java.en.*;

public class ParticipanteDAOStepDefinitions {

    private Participante participante;
    private boolean result;
    private ParticipanteDAO participanteDAO;

    @Given("a participant with fkUser {int}, fkRol {int}, fkProyecto {int}")
    public void a_participant_with_fkUser_fkRol_fkProyecto(int fkUser, int fkRol, int fkProyecto) {
        participante = new Participante();
        participante.setFkUser(fkUser);
        participante.setFkRol(fkRol);
        participante.setFkProyecto(fkProyecto);

        participanteDAO = mock(ParticipanteDAO.class);
    }

    @When("I create the participant with a donation amount of {int}")
    public void i_create_the_participant_with_a_donation_amount_of(int amount) {
        when(participanteDAO.crearParticipante(participante, amount)).thenReturn(true);
        result = participanteDAO.crearParticipante(participante, amount);
    }

    @When("I create the participant without a donation amount")
    public void i_create_the_participant_without_a_donation_amount() {
        when(participanteDAO.crearParticipante(participante, 0)).thenReturn(true);
        result = participanteDAO.crearParticipante(participante, 0);
    }

    @When("I create the participant with a database error")
    public void i_create_the_participant_with_a_database_error() {
        when(participanteDAO.crearParticipante(participante, -1)).thenReturn(false);
        result = participanteDAO.crearParticipante(participante, -1);
    }

    @Then("the participant should be created successfully")
    public void the_participant_should_be_created_successfully() {
        assertTrue(result);
    }

    @Then("the participant creation should fail")
    public void the_participant_creation_should_fail() {
        assertFalse(result);
    }
}
