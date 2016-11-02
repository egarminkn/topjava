package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by eGarmin on 31.10.2016.
 */
@RestController
@RequestMapping(value = "/ajax/profile/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealAjaxController extends AbstractMealController {
    @GetMapping
    private List<MealWithExceed> all() {
        return super.getAll();
    }

    @GetMapping("/between")
    private List<MealWithExceed> between(@RequestParam(required = false) LocalDate startDate,
                                         @RequestParam(required = false) LocalTime startTime,
                                         @RequestParam(required = false) LocalDate endDate,
                                         @RequestParam(required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @DeleteMapping("/{id}")
    private void del(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Meal> make(@RequestBody Meal jsonMeal) {
        Meal meal = super.create(jsonMeal);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().
                path(MealRestController.REST_URL + "/{id}").
                buildAndExpand(meal.getId()).toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);

        return new ResponseEntity(meal, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    private void edit(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }
}