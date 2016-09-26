package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class MealRestController {
    @Autowired
    private MealService mealService;
    @Autowired
    private UserService userService;

    private void checkUserAuthorized(int userId) {
        if (userId != AuthorizedUser.id()) {
            throw new NotFoundException("Пользователь не авторизован!");
        }
    }

    public void save(Integer id, int userId) {
        checkUserAuthorized(userId);
        Meal meal;
        if (id == null) {
            meal = new Meal(userId, LocalDateTime.now().withNano(0).withSecond(0), "", 1000);
        } else {
            meal = mealService.get(id, userId);
        }
        mealService.save(meal, userId);
    }

    public void delete(Integer id, int userId) {
        checkUserAuthorized(userId);
        if (id != null) {
            mealService.delete(id, userId);
        }
    }

    public List<MealWithExceed> getFilteredWithExceeded(int userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        checkUserAuthorized(userId);
        User user = userService.get(userId);
        return MealsUtil.getFilteredWithExceeded(mealService.getAll(userId), startDateTime, endDateTime, user.getCaloriesPerDay());
    }
}
