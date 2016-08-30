package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

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
        Map<LocalDate, Integer> dateCaloriesMap = new HashMap<>();
        for (UserMeal meal : mealList) {
            int calories = meal.getCalories();
            LocalDate date = meal.getDateTime().toLocalDate();
            if (dateCaloriesMap.containsKey(date)) {
                dateCaloriesMap.put(date, dateCaloriesMap.get(date) + calories);
            } else {
                dateCaloriesMap.put(date, calories);
            }
        }

        Map<LocalDate, Boolean> dateExceedMap = new HashMap<>();
        for (Map.Entry<LocalDate, Integer> dateCalories : dateCaloriesMap.entrySet()) {
            if (dateCalories.getValue() > caloriesPerDay) {
                dateExceedMap.put(dateCalories.getKey(), true);
            } else {
                dateExceedMap.put(dateCalories.getKey(), false);
            }
        }

        List<UserMealWithExceed> userMealWithExceeds = new LinkedList<>();
        for (UserMeal meal : mealList) {
            LocalDateTime dateTime = meal.getDateTime();
            if (TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)) {
                String description = meal.getDescription();
                int calories = meal.getCalories();
                boolean exceed = dateExceedMap.get(dateTime.toLocalDate());
                userMealWithExceeds.add(new UserMealWithExceed(dateTime, description, calories, exceed));
            }
        }

        return userMealWithExceeds;
    }
}
