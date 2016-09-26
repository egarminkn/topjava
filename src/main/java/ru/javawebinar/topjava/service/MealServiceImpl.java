package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.ExceptionUtil;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Service
public class MealServiceImpl implements MealService {
    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        return ExceptionUtil.checkNotFound(repository.save(meal, userId), "meal=" + meal + " for userId=" + userId);
    }

    @Override
    public void delete(int id, int userId) {
        ExceptionUtil.checkNotFound(repository.delete(id, userId), "id=" + id + " for userId=" + userId);
    }

    @Override
    public Meal get(int id, int userId) {
        return ExceptionUtil.checkNotFound(repository.get(id, userId), "id=" + id + " for userId=" + userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<MealWithExceed> getFilteredWithExceeded(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return repository.getFilteredWithExceeded(userId, startDate, endDate, startTime, endTime);
    }
}
