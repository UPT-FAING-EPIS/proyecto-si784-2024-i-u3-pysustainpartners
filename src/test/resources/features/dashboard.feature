Feature: Dashboard functionality
  As a user
  I want to access the dashboard
  So that I can view my projects

  Scenario: User successfully accesses the dashboard
    Given a user is authenticated with ID 1
    When the user requests the dashboard page
    Then the user's details are set in the session
    And the projects are retrieved and set in the request
