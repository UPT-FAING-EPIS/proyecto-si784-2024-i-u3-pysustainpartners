Feature: Creación de un nuevo proyecto

  Scenario: Crear un nuevo proyecto exitosamente
    Given un proyecto con los siguientes datos
      | nombre        | descripcion  | objetivo      | foto  | fkRegion | fkUser | monto | modalidades | categorias |
      | Proyecto Test | Descripción  | Objetivo Test | foto  | 1        | 1      | 1000  | 1,2         | 1,2        |
    When el usuario crea un nuevo proyecto
    Then el proyecto debe ser creado exitosamente
