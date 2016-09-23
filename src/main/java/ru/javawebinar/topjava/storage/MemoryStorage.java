package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by eGarmin on 22.09.2016.
 */
public class MemoryStorage implements Storage {
    private static volatile AtomicLong counter = new AtomicLong(0);
    private static final Storage STORAGE = new MemoryStorage();
    private Map<Long, Meal> mealMap = new ConcurrentHashMap<>();

    private MemoryStorage() {
        Meal meal;
        meal = new Meal(counter.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        mealMap.put(meal.getId(), meal);
        meal = new Meal(counter.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        mealMap.put(meal.getId(), meal);
        meal = new Meal(counter.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        mealMap.put(meal.getId(), meal);
        meal = new Meal(counter.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        mealMap.put(meal.getId(), meal);
        meal = new Meal(counter.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
        mealMap.put(meal.getId(), meal);
        meal = new Meal(counter.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
        mealMap.put(meal.getId(), meal);
    }

    public static Storage getInstance() {
        return STORAGE;
    }

    @Override
    public List<Meal> readAllMeals() {
        List<Meal> meals = new LinkedList<>();
        for (Meal meal : mealMap.values()) {
            synchronized (meal) {
                meals.add(new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories()));
            }
        }
        return meals;
    }

    @Override
    public Meal readMealById(long id) {
        return mealMap.get(id);
    }

    @Override
    public boolean deleteMealById(long id) {
        return !(!mealMap.containsKey(id) || mealMap.remove(id) == null);
    }

    @Override
    public boolean deleteMeal(Meal meal) {
        return meal.getId() != null && deleteMealById(meal.getId());
    }

    @Override
    public boolean updateMeal(Meal meal) {
        if (meal.getId() == null) {
            return false;
        }
        Meal oldMeal = readMealById(meal.getId());
        if (oldMeal == null) {
            return false;
        }
        synchronized (oldMeal) {
            oldMeal.setCalories(meal.getCalories());
            oldMeal.setDateTime(meal.getDateTime());
            oldMeal.setDescription(meal.getDescription());
        }
        return true;
    }

    @Override
    public boolean createMeal(Meal meal) {
        meal.setId(counter.getAndIncrement());
        mealMap.put(meal.getId(), meal);
        return true;
    }

    @Override
    public boolean createOrUpdateMeal(Meal meal) {
        if (meal.getId() == null) {
            return createMeal(meal);
        }
        Meal oldMeal = readMealById(meal.getId());
        if (oldMeal == null) {
            return createMeal(meal);
        }
        return updateMeal(meal);
    }
}
