# Overview

Spring for GraphQL 1.3 app to demo the following:

- Controller methods on virtual threads via `spring.threads.virtual.enabled` property.
- Synchronous client access in [ClientApp](src/main/java/com/example/activity/ClientApp.java).
- Use of DGS generated client API in [ClientDgsApp](src/main/java/com/example/activity/ClientDgsApp.java).
- Mapping to interface field `Activity.comments`.
- Schema inspection of unions and interfaces.

# Running

Run [ServerApp](src/main/java/com/example/activity/ServerApp.java) from your IDE, or
`./gradlew bootRun` from the command line to start the server.

Then use any of the following:

- Run [ClientApp](src/main/java/com/example/activity/ClientApp.java), or `./gradlew clientRun` from the command line.
- Run [ClientDgsApp](src/main/java/com/example/activity/ClientDgsApp.java), or `./gradlew clientDgsRun`.
- Run slice tests [AthleteControllerTests](src/test/java/com/example/activity/AthleteControllerTests.java) or [SearchControllerTests](src/test/java/com/example/activity/SearchControllerTests.java).
- Open GraphiQL in a browser at http://localhost:8080/graphiql.
