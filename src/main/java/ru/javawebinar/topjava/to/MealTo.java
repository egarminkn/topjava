package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import ru.javawebinar.topjava.model.Meal;

/**
 * Created by eGarmin on 21.11.2016.
 */
public class MealTo {
    private Integer id;

    @NotNull(message = "должно быть заполнено")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    @NotEmpty(message = "должно быть заполнено")
    @Size(min = 5, message = "должно быть минимум 5 символов")
    private String description;

    @NotNull(message = "должны быть указаны")
    @Range(min = 10, max = 5000, message = "должны лежать между 10 и 5000")
    private Integer calories;

    public MealTo() {
    }

    public MealTo(Meal meal) {
        this.id = meal.getId();
        this.dateTime = meal.getDateTime();
        this.description = meal.getDescription();
        this.calories = meal.getCalories();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }
}
