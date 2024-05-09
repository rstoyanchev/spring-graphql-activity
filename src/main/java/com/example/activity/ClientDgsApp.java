package com.example.activity;

import com.example.activity.codegen.types.Activity;
import com.example.activity.codegen.types.Athlete;
import com.example.activity.codegen.client.AthleteGraphQLQuery;
import com.example.activity.codegen.client.AthleteProjectionRoot;
import com.netflix.graphql.dgs.client.codegen.BaseProjectionNode;
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
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import static java.util.stream.Collectors.joining;

/**
 * {@link DgsGraphQlClient} with DGS generated client API.
 */
@Import(RestClientAutoConfiguration.class)
public class ClientDgsApp implements ApplicationRunner {

	private final DgsGraphQlClient dgsGraphQlClient;


	public ClientDgsApp(@Autowired RestClient.Builder builder) {
		RestClient restClient = builder.baseUrl("http://localhost:8080/graphql").build();
		this.dgsGraphQlClient = DgsGraphQlClient.create(HttpSyncGraphQlClient.builder(restClient).build());
	}


	public static void main(String[] args) {
		new SpringApplicationBuilder(ClientDgsApp.class).web(WebApplicationType.NONE).run(args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {

		String path = "athlete";

		System.out.println("\nSync retrieve:");
		System.out.println("==============================");

		Athlete athlete = athleteRequest("10").retrieveSync(path).toEntity(Athlete.class);
		System.out.println(formatAthlete(athlete));

		System.out.println("\nAsync:");
		System.out.println("==============================");

		Mono<Athlete> mono1 = athleteRequest("24").retrieve(path).toEntity(Athlete.class);
		Mono<Athlete> mono2 = athleteRequest("66").retrieve(path).toEntity(Athlete.class);
		Mono<Athlete> mono3 = athleteRequest("70").retrieve(path).toEntity(Athlete.class);

		Mono.zip(mono1, mono2, mono3)
				.doOnNext((tuple) -> {
					System.out.println(formatAthlete(tuple.getT1()));
					System.out.println(formatAthlete(tuple.getT2()));
					System.out.println(formatAthlete(tuple.getT3()));
				})
				.block();
	}

	private DgsGraphQlClient.RequestSpec athleteRequest(String id) {

		AthleteGraphQLQuery query = AthleteGraphQLQuery.newRequest().id(id).build();

		BaseProjectionNode projection = new AthleteProjectionRoot<>()
				.id().firstName().lastName()
				.activities().id().description().comments().text().getParent()
				.onRun().description().elevation().getParent()
				.onSwim().description().laps().indoor().getParent()
				.onRowing().description().split();

		return this.dgsGraphQlClient.request(query).projection(projection);
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
