package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
@Controller
public class MealController {
    private static final Logger LOG = LoggerFactory.getLogger(MealController.class);

    @Autowired
    private MealRestController mealController;

    @RequestMapping(value = "/meals", method = RequestMethod.POST)
    protected String doPostActionNull(HttpServletRequest request) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        final Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        if (request.getParameter("id").isEmpty()) {
            LOG.info("Create {}", meal);
            mealController.create(meal);
        } else {
            LOG.info("Update {}", meal);
            mealController.update(meal, getId(request));
        }
        return "redirect:meals";
    }

    @RequestMapping(value = "/meals/filter", method = RequestMethod.POST)
    protected String doPostActionFilter(HttpServletRequest request) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDate startDate = TimeUtil.parseLocalDate(resetParam("startDate", request));
        LocalDate endDate = TimeUtil.parseLocalDate(resetParam("endDate", request));
        LocalTime startTime = TimeUtil.parseLocalTime(resetParam("startTime", request));
        LocalTime endTime = TimeUtil.parseLocalTime(resetParam("endTime", request));
        request.setAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    protected String doGetActionNull(HttpServletRequest request) throws ServletException, IOException {
        LOG.info("getAll");
        request.setAttribute("meals", mealController.getAll());
        return "meals";
    }

    @RequestMapping(value = "/meals/delete", method = RequestMethod.GET)
    protected String doGetActionDelete(HttpServletRequest request) throws ServletException, IOException {
        int id = getId(request);
        LOG.info("Delete {}", id);
        mealController.delete(id);
        return "redirect:/meals";
    }

    @RequestMapping(value = {"/meals/create"}, method = RequestMethod.GET)
    protected String doGetActionCreate(HttpServletRequest request) throws ServletException, IOException {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "", 1000);
        request.setAttribute("meal", meal);
        return "meal";
    }

    @RequestMapping(value = {"meals/update"}, method = RequestMethod.GET)
    protected String doGetActionUpdate(HttpServletRequest request) throws ServletException, IOException {
        final Meal meal = mealController.get(getId(request));
        request.setAttribute("meal", meal);
        return "meal";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
