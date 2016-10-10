package ru.javawebinar.topjava.repository.jdbc.hsqldb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepositoryImpl;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by eGarmin on 10.10.2016.
 */
@Repository
@Profile(Profiles.HSQLDB)
public class HsqldbJdbcMealRepositoryImpl extends JdbcMealRepositoryImpl<Timestamp> {
    @Autowired
    public HsqldbJdbcMealRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Timestamp convertLocalDateTime(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
