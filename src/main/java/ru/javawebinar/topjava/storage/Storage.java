package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by eGarmin on 22.09.2016.
 */
public interface Storage {
    List<Meal> readAllMeals();
    Meal readMealById(long id);
    boolean deleteMealById(long id);
    boolean deleteMeal(Meal meal);
    boolean updateMeal(Meal meal);
    boolean createMeal(Meal meal);
    boolean createOrUpdateMeal(Meal meal);
}
