package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import java.util.Objects;
import java.io.IOException;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appContext;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appContext.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        int userId = getUserId(request);

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                             userId,
                             LocalDateTime.parse(request.getParameter("dateTime")),
                             request.getParameter("description"),
                             Integer.valueOf(request.getParameter("calories")));

        LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        mealRestController.save(meal, userId);
        response.sendRedirect("meals?userId=" + userId);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int userId = getUserId(request);
        request.setAttribute("userId", userId);

        if (action == null) {
            LOG.info("getAll");
            request.setAttribute(
                "mealList",
                mealRestController.getFilteredWithExceeded(userId, getStartDate(request), getEndDate(request), getStartTime(request), getEndTime(request))
            );
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            int id = getId(request);
            LOG.info("Delete {}", id);
            mealRestController.delete(id, userId);
            response.sendRedirect("meals?userId=" + userId);
        } else if ("create".equals(action) || "update".equals(action)) {
            Meal meal = action.equals("create") ?
                    new Meal(userId, LocalDateTime.now().withNano(0).withSecond(0), "", 1000) :
                    mealRestController.get(getId(request), userId);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    private int getUserId(HttpServletRequest request) {
        String paramUserId = Objects.requireNonNull(request.getParameter("userId"));
        return Integer.valueOf(paramUserId);
    }

    private LocalDate getDate(HttpServletRequest request, boolean isStartDate) {
        String prefixDate = isStartDate ? "start" : "end";
        String date = request.getParameter(prefixDate + "Date");
        if (date != null && !date.trim().isEmpty()) {
            return LocalDate.parse(date);
        } else {
            return isStartDate ? LocalDate.MIN : LocalDate.MAX;
        }
    }

    private LocalTime getTime(HttpServletRequest request, boolean isStartTime) {
        String prefixTime = isStartTime ? "start" : "end";
        String time = request.getParameter(prefixTime + "Time");
        if (time == null || time.trim().isEmpty()) {
            time = isStartTime ? "00:00" : "23:59";
        }
        return LocalTime.parse(time);
    }

    private LocalDate getStartDate(HttpServletRequest request) {
        return getDate(request, true);
    }

    private LocalDate getEndDate(HttpServletRequest request) {
        return getDate(request, false);
    }

    private LocalTime getStartTime(HttpServletRequest request) {
        return getTime(request, true);
    }

    private LocalTime getEndTime(HttpServletRequest request) {
        return getTime(request, false);
    }
}
