package ru.javawebinar.topjava.repository.jdbc.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepositoryImpl;

import javax.sql.DataSource;
import java.time.LocalDateTime;

/**
 * Created by eGarmin on 10.10.2016.
 */
@Repository
@Profile(Profiles.POSTGRES)
public class PostgresJdbcMealRepositoryImpl extends JdbcMealRepositoryImpl<LocalDateTime> {
    @Autowired
    public PostgresJdbcMealRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected LocalDateTime convertLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime;
    }
}
