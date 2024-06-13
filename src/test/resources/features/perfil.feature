Feature: Perfil functionality
  As a user
  I want to view my profile
  So that I can see my projects and participation

  Scenario: User successfully accesses the profile page
    Given a user is authenticated with persona ID 1
    When the user requests the profile page
    Then the user's projects are retrieved and set in the session
    And the participation count is retrieved and set in the session
    And the profile page is displayed
