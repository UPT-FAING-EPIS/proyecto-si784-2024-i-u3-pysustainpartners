Feature: Search functionality in SearchBarController

  Scenario: Successful search with a query term
    Given una busqueda con el termino "Proyecto"
    When se realiza la busqueda en el controlador
    Then los resultados deben ser los proyectos correspondientes
