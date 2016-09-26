package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import java.util.List;

/**
 * GKislin
 * 15.06.2015.
 */
public interface MealService {

    Meal save(Meal meal, int userId);

    void delete(int id, int userId);

    Meal get(int id, int userId);

    List<Meal> getAll(int userId);
}
