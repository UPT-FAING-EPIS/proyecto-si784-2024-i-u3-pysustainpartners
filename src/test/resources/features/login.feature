Feature: User Login

  Scenario: Successful login with valid captcha
    Given a user with username "user1" and password "password1"
    And a valid captcha response
    When the user attempts to log in
    Then the user is redirected to the dashboard

  Scenario: Unsuccessful login with invalid captcha
    Given a user with username "user1" and password "password1"
    And an invalid captcha response
    When the user attempts to log in
    Then an error message "Captcha inválido. Por favor, inténtelo de nuevo." is shown

  Scenario: Unsuccessful login with incorrect credentials
    Given a user with username "wronguser" and password "wrongpassword"
    And a valid captcha response
    When the user attempts to log in
    Then an error message "Usuario o contraseña incorrectos" is shown

  Scenario: Unsuccessful login with inactive user
    Given a user with username "inactiveuser" and password "password1"
    And a valid captcha response
    When the user attempts to log in
    Then an error message "Usuario inactivo" is shown
