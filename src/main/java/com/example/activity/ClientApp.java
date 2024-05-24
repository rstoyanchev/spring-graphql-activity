package com.example.activity;

import com.example.activity.athlete.Athlete;
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
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;

/**
 * Perform requests synchronously and asynchronously (for concurrent requests)
 * through {@link HttpSyncGraphQlClient} with {@link RestClient}.
 */
@Import(RestClientAutoConfiguration.class)
public class ClientApp implements ApplicationRunner {

	private static final Log logger = LogFactory.getLog(ClientApp.class);


	private final HttpSyncGraphQlClient client;


	public ClientApp(@Autowired RestClient.Builder builder) {
		RestClient restClient = builder.baseUrl("http://localhost:8080/graphql").build();
		this.client = HttpSyncGraphQlClient.builder(restClient).build();
	}


	public static void main(String[] args) {
		new SpringApplicationBuilder(ClientApp.class).web(WebApplicationType.NONE).run(args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		String document = "athlete";

		logger.debug("Sync retrieve");
		Athlete athlete = requestFor(document, 10L).retrieveSync(document).toEntity(Athlete.class);
		logger.debug(athlete);

		logger.debug("Async retrieve");
		Mono<Athlete> mono1 = requestFor(document, 24L).retrieve(document).toEntity(Athlete.class);
		Mono<Athlete> mono2 = requestFor(document, 66L).retrieve(document).toEntity(Athlete.class);
		Mono<Athlete> mono3 = requestFor(document, 70L).retrieve(document).toEntity(Athlete.class);
		Mono.zip(mono1, mono2, mono3).doOnNext(logger::debug).block();
	}

	private GraphQlClient.RequestSpec requestFor(String document, long id) {
		return this.client.documentName(document).variable("id", id);
	}

}
