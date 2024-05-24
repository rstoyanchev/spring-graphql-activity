package com.example.activity;

import java.util.List;

import com.example.activity.codegen.types.*;

import com.example.activity.search.SearchController;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@GraphQlTest(SearchController.class)
class SearchControllerTests {

	@Autowired
	private GraphQlTester graphQlTester;

	@Test
	void search() {
		this.graphQlTester.documentName("search")
				.variable("text", "ca")
				.execute()
				.path("search").entityList(SearchItem.class).satisfies(items -> {
					int i = 0;
					assertThat(club(items, i++).getName()).isEqualTo("Cambridge Harriers Athletics Club");
					assertThat(club(items, i++).getName()).isEqualTo("Cambridge University Boat Club");
					assertThat(athlete(items, i++).getLastName()).isEqualTo("Jenkins");
					assertThat(club(items, i++).getName()).isEqualTo("City of Cambridge Rowing Club");
					assertThat(athlete(items, i++).getLastName()).isEqualTo("Flatley");
					assertThat(athlete(items, i++).getLastName()).isEqualTo("Stark");
					assertThat(club(items, i++).getName()).isEqualTo("Oxford Academicals Rowing Club");
				});
	}

	private static Club club(List<SearchItem> items, int index) {
		return (Club) items.get(index);
	}

	private static Athlete athlete(List<SearchItem> items, int index) {
		return (Athlete) items.get(index);
	}
}
