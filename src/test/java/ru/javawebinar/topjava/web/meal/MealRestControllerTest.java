package ru.javawebinar.topjava.web.meal;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by eGarmin on 25.10.2016.
 */
public class MealRestControllerTest extends AbstractControllerTest {
    private static final MessageFormat REST_URL_TEMPLATE = new MessageFormat("/rest/users/{0, number,#}/meals/{1, number,#}");

    @Autowired
    private MealService mealService;

    @Test
    public void testGet() throws Exception {
        String url = REST_URL_TEMPLATE.format(new Object[] {USER_ID, MEAL1_ID + 1});
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())

                // Из-за https://jira.spring.io/browse/SPR-14472:
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(MATCHER.contentMatcher(MEAL2));
    }

    @Test
    public void testGetNotFound() throws Exception {
        String url = REST_URL_TEMPLATE.format(new Object[] {ADMIN_ID, MEAL1_ID});
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Дело плохо!"));
    }

    @Test
    public void testDelete() throws Exception {
        String url = REST_URL_TEMPLATE.format(new Object[] {USER_ID, MEAL1_ID + 1});
        mockMvc.perform(delete(url))
                .andDo(print())
                .andExpect(status().isOk());

        MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL1), mealService.getAll(USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        MessageFormat REST_URL_TEMPLATE = new MessageFormat("/rest/users/{0, number, #}/meals");
        String url = REST_URL_TEMPLATE.format(new Object[] {USER_ID});
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(new MealWithExceedListMatcher(USER, MEALS)));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL1_ID, LocalDateTime.now(), "Ланч", MEAL1.getCalories());

        String url = REST_URL_TEMPLATE.format(new Object[] {USER_ID, MEAL1_ID});
        mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());

        MATCHER.assertEquals(updated, mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void testCreate() throws Exception {
        Meal created = new Meal(LocalDateTime.now(), "Ужрак", 666);

        MessageFormat REST_URL_TEMPLATE = new MessageFormat("/rest/users/{0, number,#}/meals");
        String url = REST_URL_TEMPLATE.format(new Object[] {USER_ID});
        ResultActions resultActions =
                mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.writeValue(created)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", endsWith(url + "/" + (ADMIN_MEAL_ID + 2))))
                // Из-за https://jira.spring.io/browse/SPR-14472:
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        String json = resultActions.andReturn().getResponse().getContentAsString();
        Meal posted = JsonUtil.readValue(json, Meal.class);
        created.setId(posted.getId());

        MATCHER.assertEquals(created, posted);
        MATCHER.assertCollectionEquals(
                Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1),
                mealService.getAll(USER_ID));
    }

    @Test
    public void testGetBetweenWithAllParameters() throws Exception {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 31);
        LocalDate endDate = LocalDate.of(2015, Month.JUNE, 20);
        LocalTime startTime = LocalTime.of(13, 00);
        LocalTime endTime = LocalTime.of(20, 00);
        MessageFormat REST_URL_TEMPLATE = new MessageFormat("/rest/users/{0, number,#}/meals/filter?startDate={1}&startTime={2}&endDate={3}&endTime={4}");
        String url = REST_URL_TEMPLATE.format(new Object[] {USER_ID, startDate, startTime, endDate, endTime});
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())

                // Из-за https://jira.spring.io/browse/SPR-14472:
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(content().string(new MealWithExceedListMatcher(USER, MEALS, Arrays.asList(MEAL6, MEAL5))));
    }

    @Test
    public void testGetBetween() throws Exception {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 31);
        LocalTime startTime = LocalTime.of(13, 00);
        MessageFormat REST_URL_TEMPLATE = new MessageFormat("/rest/users/{0, number,#}/meals/filter?startDate={1}&startTime={2}&endDate=&endTime=");
        String url = REST_URL_TEMPLATE.format(new Object[] {USER_ID, startDate, startTime});
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())

                // Из-за https://jira.spring.io/browse/SPR-14472:
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(content().string(new MealWithExceedListMatcher(USER, MEALS, Arrays.asList(MEAL6, MEAL5))));
    }

    private static class MealWithExceedListMatcher extends BaseMatcher<String> {
        private List<MealWithExceed> mealWithExceedList;

        /** Конструктор
         * @param user - конкретный юзер
         * @param allMeals - вся еда юзера
         * @param meals - еда, которая будет участвовать в сравнении
         */
        public MealWithExceedListMatcher(User user, List<Meal> allMeals, List<Meal> meals) {
            Map<LocalDate, Integer> caloriesSumByDate = allMeals.stream()
                    .collect(Collectors.groupingBy(
                            Meal::getDate, Collectors.summingInt(Meal::getCalories)));

            mealWithExceedList = meals.stream()
                    .map(m -> new MealWithExceed(
                            m.getId(),
                            m.getDateTime(),
                            m.getDescription(),
                            m.getCalories(),
                            caloriesSumByDate.get(m.getDate()) > user.getCaloriesPerDay()))
                    .collect(Collectors.toList());
        }

        public MealWithExceedListMatcher(User user, List<Meal> meals) {
            this(user, meals, meals);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(JsonUtil.writeValue(mealWithExceedList));
        }

        @Override
        public boolean matches(Object actual) {
            String json = (String) actual;
            ModelMatcher<MealWithExceed> modelMatcher = new ModelMatcher<>(MealWithExceed.class);
            List<ModelMatcher<MealWithExceed>.Wrapper> expectedList = modelMatcher.wrap(mealWithExceedList);
            List<ModelMatcher<MealWithExceed>.Wrapper> actualList = modelMatcher.wrap(JsonUtil.readValues(json, MealWithExceed.class));
            return expectedList.equals(actualList);
        }
    }
}