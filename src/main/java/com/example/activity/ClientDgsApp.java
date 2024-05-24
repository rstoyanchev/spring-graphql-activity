package com.example.activity;

import com.example.activity.codegen.client.AthleteGraphQLQuery;
import com.example.activity.codegen.client.AthleteProjectionRoot;
import com.example.activity.codegen.types.Athlete;
import com.netflix.graphql.dgs.client.codegen.BaseProjectionNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.client.DgsGraphQlClient;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;

/**
 * Variation of {@link ClientApp} that uses DGS generated client API
 * through {@link HttpSyncGraphQlClient} with {@link RestClient}.
 */
@Import(RestClientAutoConfiguration.class)
public class ClientDgsApp implements ApplicationRunner {

	private static final Log logger = LogFactory.getLog(ClientDgsApp.class);


	private final DgsGraphQlClient dgsGraphQlClient;


	public ClientDgsApp(@Autowired RestClient.Builder builder) {
		RestClient restClient = builder.baseUrl("http://localhost:8080/graphql").build();
		HttpSyncGraphQlClient graphQlClient = HttpSyncGraphQlClient.builder(restClient).build();
		this.dgsGraphQlClient = DgsGraphQlClient.create(graphQlClient);
	}


	public static void main(String[] args) {
		new SpringApplicationBuilder(ClientDgsApp.class).web(WebApplicationType.NONE).run(args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {

		logger.debug("Sync retrieve");
		Athlete athlete = requestFor("10").retrieveSync("athlete").toEntity(Athlete.class);
		logger.debug(athlete);

		logger.debug("Async retrieve");
		Mono<Athlete> mono1 = requestFor("24").retrieve("athlete").toEntity(Athlete.class);
		Mono<Athlete> mono2 = requestFor("66").retrieve("athlete").toEntity(Athlete.class);
		Mono<Athlete> mono3 = requestFor("70").retrieve("athlete").toEntity(Athlete.class);
		Mono.zip(mono1, mono2, mono3).doOnNext(logger::debug).block();
	}

	private DgsGraphQlClient.RequestSpec requestFor(String id) {

		AthleteGraphQLQuery query = AthleteGraphQLQuery.newRequest().id(id).build();

		BaseProjectionNode projection = new AthleteProjectionRoot<>()
				.id().firstName().lastName()
				.activities().id().description().comments().text().getParent()
				.onRun().description().elevation().getParent()
				.onSwim().description().laps().indoor().getParent()
				.onRowing().description().split();

		return this.dgsGraphQlClient.request(query).projection(projection);
	}

}
