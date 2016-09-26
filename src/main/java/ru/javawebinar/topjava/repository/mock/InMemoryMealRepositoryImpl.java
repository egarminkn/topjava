package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        int userId = 1;
        MealsUtil.MEALS.forEach(
                meal -> {
                    meal.setUserId(userId);
                    save(meal, userId);
                }
        );
    }

    @Override
    public Meal save(Meal meal, int userId) {
        LOG.info("save " + meal + " for userId=" + userId);
        if (meal.getUserId() == null || meal.getUserId() != userId) {
            return null;
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        LOG.info("delete " + id + " for userId=" + userId);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == userId) {
            repository.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        LOG.info("get " + id + " for userId=" + userId);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == userId) {
            return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        LOG.info("getAll for userId=" + userId);
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }
}

