package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.DataNotValidException;
import ru.javawebinar.topjava.util.exception.ExceptionUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.UserUtil.prepareToSave;
import static ru.javawebinar.topjava.util.UserUtil.updateFromTo;

/**
 * GKislin
 * 06.03.2015.
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository repository;

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        prepareToSave(user);

        if (repository.getByEmail(user.getEmail()) != null) {
            throw new DataNotValidException("User with this email already present in application");
        }

        return repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void delete(int id) {
        ExceptionUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "email must not be null");
        return ExceptionUtil.checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    @Transactional
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        prepareToSave(user);

        User u = repository.getByEmail(user.getEmail());
        if (u != null && u.getId().intValue() != user.getId().intValue()) {
            throw new DataNotValidException("User with this email already present in application");
        }

        repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    public void update(UserTo userTo) {
        User u = repository.getByEmail(userTo.getEmail());

        User user = updateFromTo(get(userTo.getId()), userTo);
        prepareToSave(user);

        if (u != null && u.getId().intValue() != user.getId().intValue()) {
            throw new DataNotValidException("User with this email already present in application");
        }

        repository.save(user);
    }


    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void evictCache() {
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
        repository.save(user);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = repository.getByEmail(email.toLowerCase());
        if (u == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(u);
    }

    @Override
    public User getWithMeals(int id) {
        return ExceptionUtil.checkNotFoundWithId(repository.getWithMeals(id), id);
    }
}
