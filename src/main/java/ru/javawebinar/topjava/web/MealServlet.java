package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.storage.MemoryStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.FormatterUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by eGarmin on 22.09.2016.
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private static final Storage STORAGE = MemoryStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("MealServlet.doGet");
        List<MealWithExceed> mealsWithExceeded = MealsUtil.getFilteredWithExceeded(STORAGE.readAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("mealsWithExceeded", mealsWithExceeded);
        request.getRequestDispatcher("mealList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("MealServlet.doPost");

        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String dateTimeString = request.getParameter("dateTime");
        String description = request.getParameter("description");
        String caloriesString = request.getParameter("calories");

        try {
            Long id = Long.parseLong(idString);
            LocalDateTime dateTime = FormatterUtil.parseLocalDateTime(dateTimeString);
            int calories = Integer.parseInt(caloriesString);
            Meal meal = new Meal(id, dateTime, description, calories);
            STORAGE.createOrUpdateMeal(meal);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        response.sendRedirect("meals");
    }
}
