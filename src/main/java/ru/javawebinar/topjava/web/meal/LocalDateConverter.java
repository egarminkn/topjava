package ru.javawebinar.topjava.web.meal;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by eGarmin on 26.10.2016.
 */
public class LocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String source) {
        return source.isEmpty() ? null : LocalDate.parse(source, DateTimeFormatter.ISO_DATE);
    }
}
