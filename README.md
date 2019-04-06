# fitbit-ranking

This is the backend module of Fitbit Ranking application.

Main features of this module are:

- Publish REST services to:
    - Login using OAuth2 Fitbit service to register in the ranking
    - Get profile user information
    - Get ranking current week versus previous week
- Scheduled job to get steps information of the registred users
    
The below picture show a summary of the architecture:
     
![Architecture diagram](/diagrams/architecture.png?raw=true "Architecture diagram")

**Note 1**: highligth that every information (name, steps, token, etc.), except user's departament, is recovered from Fitbit server/services. Total previous and current steps of every user is saved in database to offer the week ranking as a service to webapp.

**Note 2**: to access to the fitbit service and to recovery information it is necessary use the access/refresh token that Fitbit Outh2 service return when user signin or the acess token expires and it's necessary using the refresh token to get another. If refresh token is revocades or there is some error when the new one is saved in the database or it happens some errors using it, the user must to signin in the ranking again or his steps will not included in the ranking.

**Note 3**: currently, application calculates week ranking. If it's necessary change the period (for instance, to month ranking) you can update service call from com.soprasteria.fitbit.scheduler.UpdateFitbitActivity.

The application uses [Fitbit's Web API](https://dev.fitbit.com/build/reference/web-api/).

You can use [Activity resource](https://dev.fitbit.com/build/reference/web-api/activity/) to get user's steps information.

For instance, to get the user's steps between 2019-04-01 and 2019-04-07:

``GET https://api.fitbit.com/1/user/-/activities/steps/date/2019-04-07/1w.json``

## Prerequisites

You need the following tools in your local development environment:

- Java 1.8.0
- Apache Maven 3.6.0
- PostgreSQL 9.3

## Webapp

The webapp (fitbit-ranking-web project) can be deployment separately, for instance using a nginx or apache instance, or can be serve by the backend application.

If you want using JVM backend to serve webapp:
 
1. Download fitbit-ranking-web project
2. Follows instruction to install and configure it
3. Generate distribution in 'build' folder using ``npm run build ``
4. Copy content of 'build' folder in the 'src/main/resources/static' folder of this project. 

## Fitbit Registration

You need a Fitbit account. If you haven't one, you can sign in [here](https://www.fitbit.com/signup).

Go to [dev.fitbit.com](https://dev.fitbit.com/login) and access with your fitbit account.

Go to [Register an application section](https://dev.fitbit.com/apps/new) and register your Fitbit application.

You must take into accoutn the below fields:
- **OAuth 2.0 Application Type**: select 'Server' value
- **Callback URL**: filling in the url login callback of your **https** url of your application. The backend application listen in http://<LOCAL_IP>:8080/login url. For instance, if you accesible domain (public or private) from browsers is 'www.ranking.com', then the callback url to fill in will be https://www.ranking.com/login
- **Default Access Type**: select 'Read-Only' value 

**NOTE**: to develop you can configure Callback URL field with 'http://localhost:8080/login'. The only case where Fitbit let you a HTTP url is when you use a localhost (for testing mode), other case you must use a HTTPS.

Go to app detail in [Manage my apps](https://dev.fitbit.com/apps) and get this fields in the application.yml (more details in next sectio):
- **OAuth 2.0 Client ID**: fill it in security.oauth2.client.clientId field
- **Client Secret**: fill it in security.oauth2.client.clientSecret
- **OAuth 2.0: Authorization URI**: review this value is in security.oauth2.client.userAuthorizationUri
- **OAuth 2.0: Access/Refresh Token Request URI**: review this value is in security.oauth2.client.accessTokenUri

**IMPORTANT**: if you reset client secret or revoke client access tokens, the users must to enroll in the aplication again. If you change Callback URL, some users could need to enroll again, if new url is no accesible and the application use access/refresh tokens. 

## Configuration

The application configuration is in the 'src/main/resources/application.yml' file.

You must fill in the next fields:

- **security.oauth2.client.clientId**: fill in with value from Fitbin Registration section
- **security.oauth2.client.clientSecret**: fill in with value from Fitbin Registration section
- **spring.datasource.***: fill in with your PostgreSQL database connection

Other interesting fileds:

- **fitbit.scheduler.cron**: cron expresion to schedule job that update steps from Fitbit to the database. For instance it is configured to run every 10 minutes. You can calculate a new crom expresion in this [page](https://www.freeformatter.com/cron-expression-generator-quartz.html)
- **logging.level.***: adjust logging level that you need (in production enviroment is recomemend only INFO level)
- **jpa.hibernate.ddl-auto**: in development enviroment can be used 'update' value, so the database schema is update every time the application start. In production mode is recommended use DDL file to create database scheme and configure this field with 'none' value

## Local enviroment

For development purpose you can use a PosgreSQL docker image:
1. ``cd postgresql``
2. ``docker build -t eg_postgresql .``
3. ``docker run --rm -p 5432:5432 --name pg_test eg_postgresql``

Local execution with:

``mvn spring-boot:run``

## Database schemas

The application has been developed and tested using PostgreSQL database.

In postgresql folder there is a ddl.sql file to create the database schema.

## Build and run

Package application in the target folder:

``mvn clean package``

JAR file generated can be execute with:

``java -jar fitbit-ranking-0.0.1-SNAPSHOT.jar``

## Docker

If you want to use Docker containers:

1. Build frontend docker image (fron 'fitbit-ranking-webapp' project):

``docker build -t fitbit-ranking-webapp . ``

2. Build bakcend docker image (from this project):

`docker build -t fitbit-ranking-backend .``

3. Up frontend, backend and database:

``docker-compose up``

4. Access to the aplication from ``http://localhost:3000``


## References

- [Fitbit Web API Reference](https://dev.fitbit.com/build/reference/web-api/)
- [Setting up PostgreSQL](https://github.com/snowplow/snowplow/wiki/Setting-up-PostgreSQL)
- [To Install Latest Oracle JDK On Linux EC2 Instance ](https://blog.knoldus.com/installing-latest-oracle-jdk-on-linux-ec2-instance-centos/)
