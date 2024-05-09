package com.example.activity.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.activity.athlete.*;
import com.example.activity.club.*;

import org.springframework.graphql.data.method.annotation.Argument;

abstract class SearchSource {

	public static List<Object> search(@Argument String text) {
		List<Object> result = new ArrayList<>();
		result.addAll(ClubSource.findClubs(text));
		result.addAll(AthleteSource.findAthletes(text));
		result.sort(new SearchItemComparator());
		return result;
	}


	private static class SearchItemComparator implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			return getComparisonString(o1).compareTo(getComparisonString(o2));
		}

		private static String getComparisonString(Object o) {
			return switch (o) {
				case Club club -> club.name();
				case Athlete athlete -> athlete.firstName();
				case null, default -> throw new IllegalArgumentException();
			};
		}
	}

}
