package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GKislin
 * 06.03.2015.
 */
@RestController
@RequestMapping(MealRestController.REST_URL)
public class MealRestController extends AbstractMealController {
    static final String REST_URL = "/rest/users/{userId}/meals";

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Дело плохо!")
    @ExceptionHandler(NotFoundException.class)
    public void notFoundException() {
        // Заворачиваем NotFoundException, вылетающие из любого метода любого контроллера,
        // в ошибку 500 с текстом "Дело плохо!"
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable("userId") int userId, @PathVariable("id") int id) {
        AuthorizedUser.setId(userId);
        return super.get(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("userId") int userId, @PathVariable("id") int id) {
        AuthorizedUser.setId(userId);
        super.delete(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll(@PathVariable int userId) {
        AuthorizedUser.setId(userId);
        return super.getAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable("userId") int userId, @PathVariable("id") int id, @RequestBody Meal meal) {
        AuthorizedUser.setId(userId);
        super.update(meal, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@PathVariable("userId") int userId, @RequestBody Meal meal) {
        AuthorizedUser.setId(userId);
        Meal created = super.create(meal);

        Map ids = new HashMap<>();
        ids.put("userId", userId);
        ids.put("id", created.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path(REST_URL + "/{id}")
                            .buildAndExpand(ids).toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getBetween(@PathVariable("userId") int userId,
                                           @RequestParam("startDate") @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
                                           @RequestParam("startTime") @DateTimeFormat(iso = ISO.TIME) LocalTime startTime,
                                           @RequestParam("endDate") @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
                                           @RequestParam("endTime") @DateTimeFormat(iso = ISO.TIME) LocalTime endTime) {
        AuthorizedUser.setId(userId);
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
