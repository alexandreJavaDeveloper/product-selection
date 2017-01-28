# Product Selection Application

## Creation of a Web Application using the following technologies:
- Spring MVC
- Spring Boot
- Spring Data JPA
- Thymeleaf
- H2 database
- Maven

## Why these technologies?

- Spring 4 MVC has several features to make the development more fast and to have easy maintenance, e.g.: Dependency injection, easy handling of routes to
create in your controller, easy handling between controller and the part of the view (use of Thymeleaf) among others.
- Still, Spring has a backend structural where uses WebSocket to connect the server and the client, where makes it easy to sending data from Controller to Frontend.
- The choose of Spring Boot explain for the fact that it comes pre-configured, have a set of frameworks/technologies to reduce the time of configuration
providing you the shortest way to have a Spring Web application. In the Spring Boot page we can have a simple RESTful application up and running
with almost zero configuration (https://spring.io/guides/gs/rest-service/).
- Spring Boot comes with Spring Data JPA, as well as the Thymeleaf and H2 database. Spring Data JPA uses the JPA API to have an integration with H2 database, beeing easy 
to create your repositories. Thymeleaf comes with Spring Boot and this is often used in HTML pages, by tags. I used this because Spring Boot creates
a bridge between server and client and then, is easy to handler the data and put into the HTML page.
- Maven is a tool that helps the developer to make build of your project, as well to choose several external libraries and put them into your application just with 
little configuration. Maven is one of the most tool used in the world to build projects.

## How to execute the Product Selection Application ( Execute via shell the following steps ):
-   git clone https://github.com/alexandreJavaDeveloper/product-selection.git
-   cd product-selection/
-   mvn clean package
-   java -jar target/sky-0.0.1-SNAPSHOT.jar

-   http://localhost:8080/productselection  #Access the address