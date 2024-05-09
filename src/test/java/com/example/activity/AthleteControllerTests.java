package com.example.activity;

import com.example.activity.athlete.AthleteController;
import com.example.activity.codegen.types.Athlete;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@GraphQlTest(AthleteController.class)
class AthleteControllerTests {

	@Autowired
	private GraphQlTester graphQlTester;

	@Test
	void getAthlete() {
		this.graphQlTester.document("query {athlete(id:10) {id, firstName, lastName}}")
				.execute()
				.path("athlete").entity(Athlete.class).satisfies(athlete -> {
					assertThat(athlete.getId()).isEqualTo("10");
					assertThat(athlete.getFirstName()).isEqualTo("June");
					assertThat(athlete.getLastName()).isEqualTo("Holt");
				});
	}

}
