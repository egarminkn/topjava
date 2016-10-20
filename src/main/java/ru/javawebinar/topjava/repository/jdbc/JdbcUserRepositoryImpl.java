package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final RowMapper<User> ROW_MAPPER_WITH_ROLES = (rs, rowNum) -> {
        User user = ROW_MAPPER.mapRow(rs, rowNum);
        Set<Role> roles = Arrays.asList(rs.getString("roles").split(", ")).stream().map(Role::valueOf).collect(Collectors.toSet());
        user.setRoles(roles);
        return user;
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUser;
    private SimpleJdbcInsert insertRoles;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("id");
        this.insertRoles = new SimpleJdbcInsert(dataSource)
                .withTableName("user_roles");
    }

    @Override
    @Transactional
    public User save(User user) {
        SqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map);

            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }

        List<SqlParameterSource> batch = user.getRoles().stream()
                .map(role -> new MapSqlParameterSource()
                        .addValue("user_id", user.getId())
                        .addValue("role", role))
                .collect(Collectors.toList());
        SqlParameterSource[] batchArray = new SqlParameterSource[batch.size()];
        insertRoles.executeBatch(batch.toArray(batchArray));

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, STRING_AGG(r.role, ', ') AS roles " +
                                              "FROM users u LEFT OUTER JOIN user_roles r ON u.id = r.user_id " +
                                              "WHERE u.id=? " +
                                              "GROUP BY u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day",
                ROW_MAPPER_WITH_ROLES, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, STRING_AGG(r.role, ', ') AS roles " +
                                              "FROM users u LEFT OUTER JOIN user_roles r ON u.id = r.user_id " +
                                              "WHERE email=? " +
                                              "GROUP BY u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day",
                ROW_MAPPER_WITH_ROLES, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day, STRING_AGG(r.role, ', ') AS roles " +
                                  "FROM users u LEFT OUTER JOIN user_roles r ON u.id = r.user_id " +
                                  "GROUP BY u.id, u.name, u.email, u.password, u.registered, u.enabled, u.calories_per_day " +
                                  "ORDER BY u.name, u.email",
                ROW_MAPPER_WITH_ROLES);
    }

}
