package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import java.util.Arrays;
import java.util.Collection;

import static ru.javawebinar.topjava.MealTestData.USER_MEAL_1;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_2;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_3;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_4;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_5;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_6;

import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

import static ru.javawebinar.topjava.MealTestData.MATCHER;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {
    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void getTestOwnMeal() throws Exception {
        Meal meal = service.get(USER_MEAL_4.getId(), USER_ID);
        MATCHER.assertEquals(meal, USER_MEAL_4);
    }

    @Test
    public void deleteTestOwnMeal() throws Exception {
        service.delete(USER_MEAL_4.getId(), USER_ID);
        MATCHER.assertCollectionEquals(
                Arrays.asList(USER_MEAL_1, USER_MEAL_2, USER_MEAL_3, USER_MEAL_5, USER_MEAL_6),
                service.getAll(USER_ID));
    }

    @Test
    public void getBetweenDatesTestOwnMeal() throws Exception {
        Collection<Meal> meals = service.getBetweenDates(
                                    LocalDate.of(2015, Month.MAY, 31),
                                    LocalDate.of(2015, Month.MAY, 31),
                                    USER_ID);
        MATCHER.assertCollectionEquals(
                Arrays.asList(USER_MEAL_1, USER_MEAL_2, USER_MEAL_3),
                meals
        );
    }

    @Test
    public void getBetweenDateTimesTestOwnMeal() throws Exception {
        Collection<Meal> meals = service.getBetweenDateTimes(
                                    LocalDateTime.of(2015, Month.MAY, 31, 10, 0),
                                    LocalDateTime.of(2015, Month.MAY, 31, 13, 0),
                                    USER_ID);
        MATCHER.assertCollectionEquals(
                Arrays.asList(USER_MEAL_2, USER_MEAL_3),
                meals
        );
    }

    @Test
    public void getAllTestOwnMeal() throws Exception {
        Collection<Meal> meals = service.getAll(USER_ID);
        MATCHER.assertCollectionEquals(
                Arrays.asList(USER_MEAL_1, USER_MEAL_2, USER_MEAL_3, USER_MEAL_4, USER_MEAL_5, USER_MEAL_6),
                meals
        );
    }

    @Test
    public void updateTestOwnMeal() throws Exception {
        Meal meal = new Meal(USER_MEAL_4);
        meal.setDescription("Обновленная пища");
        service.update(meal, USER_ID);
        MATCHER.assertEquals(meal, service.get(USER_MEAL_4.getId(), USER_ID));
    }

    @Test
    public void saveTestOwnMeal() throws Exception {
        Meal meal = new Meal(USER_MEAL_4);
        meal.setId(null);
        meal.setDescription("Новая пища");
        int id = service.save(meal, ADMIN_ID).getId();
        meal.setId(id);
        MATCHER.assertEquals(meal, service.get(id, ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void getTestForeignMeal() throws Exception {
        service.get(USER_MEAL_4.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteTestForeignMeal() throws Exception {
        service.delete(USER_MEAL_4.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateTestForeignMeal() throws Exception {
        Meal meal = new Meal(USER_MEAL_4);
        meal.setDescription("Обновленная пища");
        service.update(meal, ADMIN_ID);
    }
}