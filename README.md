<h1 align="center">Hi there, I'm <a href="https://daniilshat.ru/" target="_blank">Artur</a> 
<img src="https://github.com/blackcater/blackcater/raw/main/images/Hi.gif" height="32"/></h1>
<h3 align="center">News management system
This application was created in order to fulfill the control task for the development of an application for 
managing news and comments, based on microservice architecture. 🇷🇺</h3>


## Purpose of development
REST ful web-service that implements the functionality f
or working with the news management system.

## Project Requirements
* JDK 17+
* Gradle 7.6+
* Docker Engine 20.10.23+

## Project Tech Stack
* Java 17
* Gradle 7.6
* Git
* Liquibase
* Redis
* JUnit 5
* Mockito
* TestContainers
* WireMock
* Logback
* Lombok
* Swagger
* Docker
* Spring Boot 3
* Spring Data JPA
* Spring MVC
* Spring Security
* Spring AOP
* PostgreSQL


## How to start
#### Clone the project from the repository
git clone https://github.com/ARTURITPRO/news-applications
#### Build Application
gradlew build

#### Run news and user services
-java -jar news-service/build/libs/news-service.jar  
-java -jar user-service/build/libs/user-service.jar

### Run application in docker:
docker-compose up

## Modules

### Exception Starter:
A module that provides a global exception handler and stores all types of exceptions for the application.

### Log Starter:
A module that provides a LogController annotation for request-response logging at the Rest Controller level.
The service also provides logging by levels in all layers of the application

### News Service:
A service that implements a news management system.  
**Available endpoints**:  
*GET /news_applications/v1/news?keyword=%25City%25* - Get All News With CriteriaString
*GET /news_applications/v1/news* - Get All News & Default Page Size=10 & Default PageNo=0  
*POST /news_applications/v1/news* - Create news  
*PUT /news_applications/v1/news/{id}* - Update news
*DELETE /news_applications/v1/news//{id}* - Delete news by id 

*GET /news_applications/v1/comments* - Find all comments  
*GET /news_applications/v1/comment/{id}* - Find comment by id  
*GET /news_applications/v1/comment/{newsId}/comment* - Find all comments by specifying news id  
*POST /news_applications/v1/comment/{newsId}/comments* - Create comment for a specific news  
*PUT /news_applications/v1/comment/{id}* - Update comment by id  
*DELETE /news_applications/v1/comment/{id}* - Delete comment by id  

**Possible profiles**:  
*custom-cache*: Run application with custom LRU or LFU implementations  
*redis*: Run application with a cache implementation in the form of Redis DB

### User Service:
Service for user authorization and authentication. Issues a token based on the user's role.
Possible roles: admin, journalist, subscriber.

**Available endpoints**:  
POST api/auth/login - user authorization and authentication  
POST api/auth/register - Register a new user  
GET api/auth/user/{token} - get user by token  
POST api/auth/token - refresh user token  
POST api/auth/refresh - refresh user refresh token  

#### Swagger OpenAPI documentation:
http://localhost:8080/swagger-ui.html

