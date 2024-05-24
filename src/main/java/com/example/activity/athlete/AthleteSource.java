package com.example.activity.athlete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static com.example.activity.FakerUtils.aBoolean;
import static com.example.activity.FakerUtils.anInt;
import static com.example.activity.FakerUtils.mood;
import static com.example.activity.FakerUtils.shakespeare;
import static com.example.activity.FakerUtils.weather;
import static java.util.stream.Collectors.toMap;

public abstract class AthleteSource {

	/* Athletes data */

	private static final List<Athlete> athletes = List.of(
			new Athlete(1L, "Nestor", "Holt"),
			new Athlete(3L, "Jose", "Graham"),
			new Athlete(5L, "Carmen", "Jenkins"),
			new Athlete(6L, "Josh", "Hamill"),
			new Athlete(8L, "Miguel", "Harris"),
			new Athlete(10L, "June", "Holt"),
			new Athlete(12L, "Tomas", "Ware"),
			new Athlete(14L, "Wally", "Cohen"),
			new Athlete(17L, "Diana", "Ray"),
			new Athlete(20L, "Bryan ", "Duarte"),
			new Athlete(24L, "Jamey", "Blackwell"),
			new Athlete(25L, "Heidi", "Hammond"),
			new Athlete(27L, "Kelley", "Bowman"),
			new Athlete(32L, "Don", "Montes"),
			new Athlete(35L, "Jordan", "Page"),
			new Athlete(39L, "Paula", "Conrad"),
			new Athlete(45L, "Joshua", "Moss"),
			new Athlete(51L, "Jessica", "Stark"),
			new Athlete(58L, "Andrea", "Barnes"),
			new Athlete(61L, "Tom", "Hansen"),
			new Athlete(64L, "Jerica", "Flatley"),
			new Athlete(66L, "Jodi", "Smith"),
			new Athlete(67L, "Bill", "Stroman"),
			new Athlete(70L, "Jerry", "Reynolds"),
			new Athlete(74L, "Shelly", "Hoppe"),
			new Athlete(75L, "Layne", "Smitham"),
			new Athlete(79L, "Joan", "Davis"),
			new Athlete(81L, "Shelby", "Russel")
	);

	private static final Map<Long, Athlete> athleteLookup =
			athletes.stream().collect(toMap(Athlete::id, Function.identity()));


	/* Activity data */

	private static final Map<Athlete, List<Activity>> activityLookup =
			athletes.stream().collect(toMap(Function.identity(), a -> generateActivities()));

	private static long activityIndex = 1;

	private static List<Activity> generateActivities() {
		List<Activity> list = new ArrayList<>();
		for (int i = 0; i < anInt(1, 4); i++) {
			switch (anInt(0, 2)) {
				case 0: list.add(new Run(activityIndex++, description(Run.class), anInt(0, 300)));
				case 1: list.add(new Swim(activityIndex++, description(Swim.class), anInt(0, 160), aBoolean()));
				case 2: list.add(new Rowing(activityIndex++, description(Rowing.class), anInt(90, 180)));
			}
		}
		return list;
	}

	private static String description(Class<? extends Activity> activity) {
		return weather.description() + ", " + mood.emotion() + " " + activity.getSimpleName().toLowerCase();
	}


	/* Comment data */

	private static final Map<Activity, List<Comment>> commentsLookup =
			activityLookup.values().stream().flatMap(Collection::stream)
					.collect(toMap(Function.identity(), a -> generateComments()));

	private static List<Comment> generateComments() {
		Set<Comment> set = new HashSet<>();
		int targetSize = anInt(1, 3);
		while (set.size() < targetSize) {
			set.add(new Comment(shakespeare.asYouLikeItQuote()));
		}
		return new ArrayList<>(set);
	}


	/* Access methods */

	public static List<Athlete> findAthletes(String text) {
		String textLowerCase = text.toLowerCase();
		return athletes.stream()
				.filter(a -> {
					String first = a.firstName().toLowerCase();
					String last = a.lastName().toLowerCase();
					return (first.contains(textLowerCase) || last.contains(textLowerCase));
				})
				.toList();
	}

	public static Athlete getAthlete(Long id) {
		return athleteLookup.get(id);
	}

	public static List<List<Activity>> getActivities(List<Athlete> athletes) {
		return delay(() -> athletes.stream().map(activityLookup::get).toList());
	}

	public static List<Activity> getActivities(Athlete athlete) {
		return delay(() -> activityLookup.get(athlete));
	}

	public static List<List<Comment>> getComments(List<Activity> activities) {
		return delay(() -> activities.stream().map(commentsLookup::get).toList());
	}

	public static List<Comment> getComments(Activity activity) {
		return delay(() -> commentsLookup.get(activity));
	}

	private static <T> T delay(Callable<T> callable) {
		try {
			Thread.sleep(1000);
			return callable.call();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
