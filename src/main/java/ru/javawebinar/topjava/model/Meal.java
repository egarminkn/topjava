package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * GKislin
 * 11.01.2015.
 */
@Entity
@Table(
        name = "meals",
        uniqueConstraints = @UniqueConstraint(
                                name = "meals_unique_user_datetime_idx",
                                columnNames = {"user_id", "date_time"}))
@NamedQueries({
    @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal meal WHERE meal.id=:id AND meal.user=:user"),
    @NamedQuery(name = Meal.FIND, query = "SELECT meal FROM Meal meal WHERE meal.id=?50 AND meal.user=:user"),
    @NamedQuery(name = Meal.FIND_ALL_SORTED, query = "SELECT meal FROM Meal meal WHERE meal.user=:user ORDER BY meal.dateTime DESC"),
    @NamedQuery(name = Meal.FIND_BETWEEN_SORTED, query = "SELECT meal FROM Meal meal WHERE meal.user=:user AND meal.dateTime BETWEEN ?1 AND ?2 ORDER BY meal.dateTime DESC")
})
public class Meal extends BaseEntity {
    public static final String DELETE = "Meal.delete";
    public static final String FIND = "Meal.get";
    public static final String FIND_ALL_SORTED = "Meal.getAll";
    public static final String FIND_BETWEEN_SORTED = "Meal.getBetween";

    @Column(name = "date_time", nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT NOT NULL")
    @NotEmpty
    @Length(max = 255)
    private String description;

    @Column(name = "calories", nullable = false, columnDefinition = "INT NOT NULL")
    @NotNull
    @Digits(fraction = 0, integer = 4)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
