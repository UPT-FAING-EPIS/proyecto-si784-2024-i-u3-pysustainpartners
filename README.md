# Proyecto Calidad Final



Este proyecto está configurado para generar reportes de tests tanto de Cucumber como de cobertura de tests unitarios.

## Link del video publicado en YouTube
https://youtu.be/C8y1bsTbU1I

## Generar Reportes de Tests

Para generar los reportes de tests, ejecuta el siguiente comando Maven:

```sh
mvn clean test jacoco:report
```
Este comando realizará las siguientes acciones:
1. Limpiará el proyecto.
2. Ejecutará los tests.
3. Generará el reporte de cobertura de código utilizando JaCoCo.

## Ubicación de los Reportes

### Reporte de Tests Unitarios

El reporte de cobertura de los tests unitarios se encuentra en la carpeta `target/site` y puede ser visualizado abriendo el archivo `index.html` en un navegador web.

Ruta: `target/site/index.html`

### Reporte de Tests de Cucumber

El reporte de los tests de Cucumber se encuentra en la carpeta `target` y puede ser visualizado abriendo el archivo `cucumber-reports.html` en un navegador web.

Ruta: `target/cucumber-reports.html`
