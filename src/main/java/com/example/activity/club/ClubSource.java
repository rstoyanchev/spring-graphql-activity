package com.example.activity.club;

import java.util.List;

import static com.example.activity.FakerUtils.anInt;

public abstract class ClubSource {

	private static final List<Club> clubs = List.of(
			new Club(1L, "Riverside Runners", memberCount()),
			new Club(2L, "Newnham Riverbank Club", memberCount()),
			new Club(3L, "London City Runners", memberCount()),
			new Club(7L, "Jesus Green Lido", memberCount()),
			new Club(10L, "City of Cambridge Rowing Club", memberCount()),
			new Club(12L, "Oxford Academicals Rowing Club", memberCount()),
			new Club(15L, "Parkside Pool and Gym", memberCount()),
			new Club(18L, "The Running Club London", memberCount()),
			new Club(21L, "Cambridge University Boat Club", memberCount()),
			new Club(23L, "Cambridge Harriers Athletics Club", memberCount()),
			new Club(27L, "City of Oxford Swimming Club", memberCount()),
			new Club(29L, "East End Road Runners", memberCount()),
			new Club(30L, "City of Oxford Rowing Club", memberCount()),
			new Club(33L, "Dulwich Park Runners", memberCount())
	);


	public static List<Club> findClubs(String text) {
		String textLowerCase = text.toLowerCase();
		return clubs.stream().filter(club -> club.name().toLowerCase().contains(textLowerCase)).toList();
	}

	private static int memberCount() {
		return anInt(50, 500);
	}

}
