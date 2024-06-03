Feature: Gestionar Persona
  Scenario: Crear una nueva persona
    Given un usuario con los siguientes datos
      | nombre  | apellido | celular     | fechaNacimiento | sexo | fkUser |
      | Juan    | Perez    | 123456789   | 2000-01-01      | M    | 1      |
    When el usuario crea una nueva persona
    Then la persona debe ser creada exitosamente
