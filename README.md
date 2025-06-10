# keycloak-testing-workflow-demo

Este repositorio contiene una demostración de un flujo de trabajo de CI/CD para el despliegue y la prueba de un servidor Keycloak. Incluye pruebas de backend con Postman (Newman) y pruebas de frontend con Selenium JS.

## Contenido del Repositorio

* `.github/workflows/deploy-and-test.yml`: Definición del flujo de trabajo de GitHub Actions para el despliegue y las pruebas.
* `KeycloakProviders/`: Este directorio está destinado a contener los módulos SPI (Service Provider Interface) de Keycloak. El flujo de trabajo construye los archivos JAR de estos módulos.
* `Testing/Backend/UserPasswordAuth/BasicKeycloakTest.postman_collection.json`: Una colección de Postman que contiene pruebas de backend para la autenticación de usuario/contraseña en Keycloak.
* `Testing/Backend/UserPasswordAuth/BasicTestEnvironment.postman_environment.json`: Un archivo de entorno de Postman con las variables necesarias para ejecutar las pruebas de backend.
* `Testing/Frontend/SeleniumJs/LoginToWelcome.test.mjs`: Pruebas de frontend utilizando Selenium WebDriver para verificar el inicio de sesión y la navegación a la página de bienvenida de Keycloak.
* `Testing/Frontend/SeleniumJs/package.json`: Define las dependencias de Node.js y los scripts para las pruebas de Selenium.
* `Testing/Frontend/SeleniumJs/package-lock.json`: Archivo de bloqueo de dependencias para las pruebas de Selenium.
* `.gitignore`: Archivo para ignorar directorios y archivos específicos del control de versiones.

## Flujo de Trabajo (GitHub Actions)

El flujo de trabajo `deploy-and-test.yml` se ejecuta automáticamente en los siguientes eventos:

* `push` a la rama `main`
* `pull_request` a la rama `main`
* `workflow_dispatch` (ejecución manual)

El flujo de trabajo consta de los siguientes trabajos:

### 1. `build-and-deploy`

Este trabajo es responsable de construir los módulos SPI de Keycloak y prepararlos para el despliegue.

* **Configuración**:
    * Se ejecuta en `ubuntu-latest`.
    * Utiliza `actions/checkout@v4` para clonar el repositorio.
    * Configura Java 17 usando `actions/setup-java@v4`.
    * Instala Maven (nota: esto se marca como "Solo se usa localmente" en el flujo de trabajo, pero se ejecuta en el runner de GitHub Actions).
    * Construye todos los módulos SPI encontrados en el directorio `KeycloakProviders` utilizando `mvn clean package -DskipTests`.
    * Prepara los archivos JAR construidos para el despliegue a los nodos de Keycloak especificados en `secrets.KEYCLOAK_NODES`.

### 2. `postman-test-runner`

Este trabajo ejecuta las pruebas de backend de Postman utilizando Newman.

* **Dependencias**: Necesita que el trabajo `build-and-deploy` se complete exitosamente (`needs: build-and-deploy`).
* **Configuración**:
    * Se ejecuta en `ubuntu-latest`.
    * Utiliza `actions/checkout@master`.
    * Ejecuta la colección de Postman `BasicKeycloakTest.postman_collection.json` con el entorno `BasicTestEnvironment.postman_environment.json` utilizando `matt-ball/newman-action@master`.
    * Las pruebas incluyen verificar un estado 200 OK y la presencia de un `access_token` en la respuesta.
    * Las variables `url`, `realm`, `username`, `password`, y `clientId` para las pruebas de Postman se definen en el archivo de entorno `BasicTestEnvironment.postman_environment.json`.

### 3. `selenium-js-test`

Este trabajo ejecuta las pruebas de frontend utilizando Selenium WebDriver con Node.js.

* **Dependencias**: Necesita que el trabajo `build-and-deploy` se complete exitosamente (`needs: build-and-deploy`).
* **Configuración**:
    * Se ejecuta en `ubuntu-latest`.
    * Configura un servicio de Selenium Chrome Standalone (`selenium/standalone-chrome:latest`) accesible en el puerto 4444.
    * Define variables de entorno como `SERVER_URL`, `REALM`, `CLIENT_ID`, `TEST_USERNAME`, y `TEST_PASSWORD`.
    * Clona el repositorio usando `actions/checkout@v4`.
    * Configura Node.js versión 20 usando `actions/setup-node@v4`.
    * Instala las dependencias de Node.js en el directorio `Testing/Frontend/SeleniumJs`.
    * Ejecuta las pruebas de Selenium JS usando `npm test` en el directorio `Testing/Frontend/SeleniumJs`.
    * La prueba `LoginToWelcome.test.mjs` simula el inicio de sesión en Keycloak y verifica que se llega a la página de bienvenida. Captura capturas de pantalla si una prueba falla.

## Módulos SPI de Keycloak

El directorio `KeycloakProviders` está diseñado para albergar módulos SPI personalizados de Keycloak. Los archivos JAR resultantes de la construcción de estos módulos se identifican y se pueden desplegar en los nodos de Keycloak.

## Variables Secretas

El flujo de trabajo utiliza la variable secreta `KEYCLOAK_NODES`. Asegúrate de configurar esta secreta en tu repositorio de GitHub para que el paso de despliegue pueda acceder a los nodos de Keycloak. Se espera que esta variable contenga una lista de hosts de Keycloak.
