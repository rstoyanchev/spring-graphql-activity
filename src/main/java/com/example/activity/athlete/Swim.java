package com.example.activity.athlete;

public record Swim(Long id, String description, int laps, boolean indoor) implements Activity {
}
