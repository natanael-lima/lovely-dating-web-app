# Lovely - Aplicación de Citas

Lovely es una aplicación de citas similar a Tinder, que permite a los usuarios registrarse y crear perfiles detallados. Con un buscador integrado, los usuarios pueden encontrar coincidencias basadas en preferencias configuradas. Una vez emparejados, pueden chatear en tiempo real utilizando WebSocket. La función de match mutuo desencadena conversaciones significativas. Además, los usuarios pueden editar perfiles, cambiar preferencias y acceder a opciones de seguridad en la sección de perfil. LovelyApp proporciona una plataforma intuitiva y segura.

## Características Principales

- **Registro y Perfil de Usuario:** Los usuarios pueden registrarse y crear perfiles detallados con información personal y preferencias.
- **Buscador de Coincidencias:** Encuentra coincidencias basadas en preferencias configuradas, como edad, ubicación y género.
- **Match Mutuo:** La aplicación permite a los usuarios hacer match con otros usuarios y comenzar una conversación.
- **Chat en Tiempo Real:** Utiliza WebSocket para permitir conversaciones en tiempo real entre usuarios que han hecho match.
- **Edición de Perfil:** Los usuarios pueden editar su perfil, ajustar sus preferencias y acceder a opciones de seguridad en cualquier momento.

## Tecnologías Utilizadas

- **Backend:**
  - [Java](https://www.java.com/): Lenguaje de programación utilizado para desarrollar la lógica del backend.
  - [Spring Boot](https://spring.io/projects/spring-boot): Framework para construir aplicaciones Java robustas y escalables.
  - [Spring Security](https://spring.io/projects/spring-security): Framework para la autenticación y autorización de usuarios.
  - [Spring Data JPA](https://spring.io/projects/spring-data-jpa): Subproyecto de Spring para trabajar con bases de datos relacionales.
  - [JWT (JSON Web Tokens)](https://jwt.io/): Utilizado para la autenticación segura en la aplicación.
  - [WebSocket](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API): Protocolo para comunicación en tiempo real.

- **Frontend:**
  - [Angular 17](https://angular.io/): Framework de desarrollo frontend para construir interfaces de usuario dinámicas.
  - [HTML](https://developer.mozilla.org/en-US/docs/Web/HTML): Lenguaje de marcado para estructurar páginas web.
  - [CSS](https://developer.mozilla.org/en-US/docs/Web/CSS): Lenguaje de estilos para diseñar y dar formato a las páginas web.

- **Base de Datos:**
  - [MySQL](https://www.mysql.com/): Sistema de gestión de bases de datos relacional para almacenar la información de usuarios, perfiles y mensajes.

- **APIs y Herramientas:**
  - [API REST](https://restfulapi.net/): Arquitectura utilizada para desarrollar servicios web escalables.
  - [Postman](https://www.postman.com/): Herramienta para probar y documentar APIs.

## Instalación y Configuración

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/natanael-lima/lovely-dating-web-app.git
   cd lovely-dating-web-app
   ## :
2. **Configuración del Backend**
    - Configura el archivo `application.properties` con los detalles de tu base de datos MySQL.  
    ```bash
    mvn spring-boot:run

4. **Configuración del Frontend:**
    ```bash
    cd frontend
    npm install
    ng serve
5. **Acceso a la Aplicación:**
   - Accede a la aplicación en tu navegador en http://localhost:4200.

## Vista Previa del Proyecto

![Vista previa de la aplicación](https://i.postimg.cc/J4QznsVc/project-lovely-v2.png)

## Contribuciones

Las contribuciones son bienvenidas. Si deseas contribuir a este proyecto, por favor realiza un fork del repositorio, crea una rama con tus cambios y envía un pull request.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Para más detalles, consulta el archivo LICENSE.

## Contacto

Si tienes alguna pregunta o sugerencia, no dudes en ponerte en contacto.

- **Autor:** Natanael Lima
- **Repositorio:** [GitHub](https://github.com/natanael-lima/lovely-dating-web-app)




