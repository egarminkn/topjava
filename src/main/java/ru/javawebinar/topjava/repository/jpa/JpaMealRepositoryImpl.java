package ru.javawebinar.topjava.repository.jpa;

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
 * User: gkisline
 * Date: 26.08.2014
 */

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User user = em.getReference(User.class, userId);
            meal.setUser(user);
            em.persist(meal);
            return meal;
        } else {
            Meal oldMeal = em.getReference(Meal.class, meal.getId());
            User user = oldMeal.getUser();
            if (user != null && user.getId() == userId) {
                meal.setUser(user);
                return em.merge(meal);
            } else {
                return null;
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        User user = em.getReference(User.class, userId);
        return em.createNamedQuery(Meal.DELETE).setParameter("id", id).setParameter("user", user).executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        User user = em.getReference(User.class, userId);
        List<Meal> meals = em.createNamedQuery(Meal.FIND, Meal.class).setParameter(50, id).setParameter("user", user).getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        User user = em.getReference(User.class, userId);
        return em.createNamedQuery(Meal.FIND_ALL_SORTED, Meal.class).setParameter("user", user).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        User user = em.getReference(User.class, userId);
        return em.createNamedQuery(Meal.FIND_BETWEEN_SORTED, Meal.class)
                .setParameter("user", user)
                .setParameter(1, startDate).setParameter(2, endDate)
                .getResultList();
    }
}