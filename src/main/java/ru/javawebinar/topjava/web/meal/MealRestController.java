package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class MealRestController {
    private final MealService mealService;

    @Autowired
    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    private void checkUserAuthorized(int userId) {
        if (userId != AuthorizedUser.id()) {
            throw new NotFoundException("Пользователь не авторизован!");
        }
    }

    public void save(Meal meal, int userId) {
        checkUserAuthorized(userId);
        mealService.save(meal, userId);
    }

    public void delete(Integer id, int userId) {
        checkUserAuthorized(userId);
        if (id != null) {
            mealService.delete(id, userId);
        }
    }

    public List<MealWithExceed> getFilteredWithExceeded(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        checkUserAuthorized(userId);
        return mealService.getFilteredWithExceeded(userId, startDate, endDate, startTime, endTime);
    }

    public Meal get(int id, int userId) {
        checkUserAuthorized(userId);
        return mealService.get(id, userId);
    }
}
