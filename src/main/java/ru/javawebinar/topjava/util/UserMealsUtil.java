package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> dateCaloriesMap = new HashMap<>();
        mealList.forEach(meal -> {
            int calories = meal.getCalories();
            LocalDate date = meal.getDateTime().toLocalDate();
            if (dateCaloriesMap.containsKey(date)) {
                dateCaloriesMap.put(date, dateCaloriesMap.get(date) + calories);
            } else {
                dateCaloriesMap.put(date, calories);
            }
        });

        final Map<LocalDate, Boolean> dateExceedMap = new HashMap<>();
        dateCaloriesMap.forEach((date, calories) -> {
            if (calories > caloriesPerDay) {
                dateExceedMap.put(date, true);
            } else {
                dateExceedMap.put(date, false);
            }
        });

        return mealList.parallelStream()
            .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
            .map(meal -> {
                boolean exceed = dateExceedMap.get(meal.getDateTime().toLocalDate());
                return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed);
            })
            .collect(Collectors.toList());
    }
}
