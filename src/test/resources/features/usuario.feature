Feature: UsuarioController functionality

  Scenario: Successful search with a query term
    Given una búsqueda de usuario con el término "user"
    When se realiza la búsqueda de usuario en el controlador
    Then los resultados deben ser los usuarios correspondientes

  Scenario: Successful role update for a user
    Given una solicitud para cambiar el rol del usuario con ID 1 a 2
    When se realiza la actualización de rol en el controlador
    Then el rol del usuario debe ser actualizado correctamente
