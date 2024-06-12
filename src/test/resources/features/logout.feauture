Feature: Logout functionality
  As a user
  I want to log out from the application
  So that I can end my session securely

  Scenario: User successfully logs out
    Given an authenticated user with ID 1
    When the user requests the logout page
    Then the session is invalidated
    And the user is redirected to the login page
