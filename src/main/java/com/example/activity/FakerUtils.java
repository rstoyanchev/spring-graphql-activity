package com.example.activity;

import net.datafaker.Faker;
import net.datafaker.providers.base.Mood;
import net.datafaker.providers.base.Shakespeare;
import net.datafaker.providers.base.Weather;

public abstract class FakerUtils {

	private static final Faker faker = new Faker();

	public static final Weather weather = faker.weather();

	public static final Mood mood = faker.mood();

	public static final Shakespeare shakespeare = faker.shakespeare();


	public static int anInt(int min, int max) {
		return faker.number().numberBetween(min, max);
	}

	public static boolean aBoolean() {
		return faker.random().nextBoolean();
	}

}
