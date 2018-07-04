package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.storage.StorageMeals;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Meal {

    private final int id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.id = StorageMeals.nextID.incrementAndGet();
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate(){
        return dateTime.toLocalDate();
    }

    public int getId() {
        return id;
    }


}
