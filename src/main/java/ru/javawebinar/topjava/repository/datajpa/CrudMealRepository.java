package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

/**
 * gkislin
 * 02.10.2016
 */
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Modifying
    @Query(name = Meal.DELETE)
    int delete(@Param("id") int id,
               @Param("userId") int userId);

    @Query(name = Meal.GET)
    List<Meal> findOne(@Param("id") int id,
                       @Param("userId") int userId);

    @Query(name = Meal.ALL_SORTED)
    List<Meal> findAll(@Param("userId") int userId);

    @Query(name = Meal.GET_BETWEEN)
    List<Meal> findBetween(@Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate,
                           @Param("userId") int userId);
}
