# Overview

Demo of Spring for GraphQL 1.3 features:

- Imperative controller methods on virtual threads with Java 21+ and `spring.threads.virtual.enabled=true`.
- Client access with blocking API using `HttpSyncGraphQlClient` and `RestClient` as the transport.
- Client access with `DgsGraphQlClient` and DGS generated client API.
- Mapping support for interface fields, e.g. `Activity.comments`.
- Schema inspection support for union and interface types.

# Run the Demo

Start the server by running `ServerApp` from your IDE, or via `./gradlew bootRun` from the command line.

Use GraphiQL in a browser at http://localhost:8080/graphiql to run queries.

Run the `ClientApp` or `ClientDgsApp` apps from your IDE, or from the commandline via
`./gradlew clientRun` and `./gradlew clientDgsRun`.
