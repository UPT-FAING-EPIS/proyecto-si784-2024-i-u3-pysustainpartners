Feature: ParticipanteDAO functionality

  Scenario: Create a participant with donor role
    Given a participant with fkUser 1, fkRol 1, fkProyecto 1
    When I create the participant with a donation amount of 100
    Then the participant should be created successfully

  Scenario: Create a participant with non-donor role
    Given a participant with fkUser 2, fkRol 2, fkProyecto 2
    When I create the participant without a donation amount
    Then the participant should be created successfully

  Scenario: Fail to create a participant due to database error
    Given a participant with fkUser 3, fkRol 3, fkProyecto 3
    When I create the participant with a database error
    Then the participant creation should fail
