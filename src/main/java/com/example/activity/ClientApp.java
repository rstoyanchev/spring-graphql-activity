package com.example.activity;

import com.example.activity.codegen.types.Activity;
import com.example.activity.codegen.types.Athlete;
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
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import static java.util.stream.Collectors.joining;

/**
 * Perform requests synchronously and asynchronously (for concurrent requests)
 * through {@link HttpSyncGraphQlClient} with {@link RestClient}.
 */
@Import(RestClientAutoConfiguration.class)
public class ClientApp implements ApplicationRunner {

	private final HttpSyncGraphQlClient graphQlClient;


	public ClientApp(@Autowired RestClient.Builder builder) {
		RestClient restClient = builder.baseUrl("http://localhost:8080/graphql").build();
		this.graphQlClient = HttpSyncGraphQlClient.builder(restClient).build();
	}


	public static void main(String[] args) {
		new SpringApplicationBuilder(ClientApp.class).web(WebApplicationType.NONE).run(args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {

		String path = "athlete";

		System.out.println("\nSync retrieve:");
		System.out.println("==============================");

		Athlete athlete = athleteRequest(10L).retrieveSync(path).toEntity(Athlete.class);
		System.out.println(formatAthlete(athlete));

		System.out.println("\nAsync:");
		System.out.println("==============================");

		Mono<Athlete> mono1 = athleteRequest(24L).retrieve(path).toEntity(Athlete.class);
		Mono<Athlete> mono2 = athleteRequest(66L).retrieve(path).toEntity(Athlete.class);
		Mono<Athlete> mono3 = athleteRequest(70L).retrieve(path).toEntity(Athlete.class);

		Mono.zip(mono1, mono2, mono3)
				.doOnNext((tuple) -> {
					System.out.println(formatAthlete(tuple.getT1()));
					System.out.println(formatAthlete(tuple.getT2()));
					System.out.println(formatAthlete(tuple.getT3()));
				})
				.block();
	}

	private GraphQlClient.RequestSpec athleteRequest(long id) {
		return this.graphQlClient.documentName("GetAthlete").variable("id", id);
	}

	private static String formatAthlete(@Nullable Athlete athlete) {
		return "\n" + (athlete != null ? athlete.getFirstName() + " " + athlete.getLastName() + ", " +
				"(id=" + athlete.getId() + "). " + formatActivities(athlete) : null);
	}

	private static String formatActivities(Athlete athlete) {
		return (!CollectionUtils.isEmpty(athlete.getActivities()) ?
				"Activities:" + athlete.getActivities().stream()
						.map(activity -> "> " + activity.getDescription() + ". " + formatComments(activity))
						.collect(joining("\n\t", "\n\t", "")) : ". ");
	}

	private static String formatComments(Activity activity) {
		return (!CollectionUtils.isEmpty(activity.getComments()) ?
				"Comments:" + activity.getComments().stream()
						.map(comment -> "- " + comment.getText()).collect(joining("\n\t\t", "\n\t\t", "")) : "");
	}

}
