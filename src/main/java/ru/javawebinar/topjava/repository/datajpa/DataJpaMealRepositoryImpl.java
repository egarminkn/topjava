package ru.javawebinar.topjava.repository.datajpa;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GKislin
 * 27.03.2015.
 */
@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepositoryImpl implements MealRepository {

    @Autowired
    private CrudMealRepository crudRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(em.getReference(User.class, userId));
        return crudRepository.save(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = crudRepository.findOne(id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public Meal getEager(int id, int userId) {
        Meal meal = get(id, userId);
        if (meal != null) {
            Hibernate.initialize(meal.getUser()); // Вытаскиваем LAZY-юзера
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.findBetween(startDate, endDate, userId);
    }
}
