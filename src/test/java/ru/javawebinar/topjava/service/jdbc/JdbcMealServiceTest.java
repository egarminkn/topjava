package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

/**
 * Created by eGarmin on 10.10.2016.
 */
@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends MealServiceTest {
}
