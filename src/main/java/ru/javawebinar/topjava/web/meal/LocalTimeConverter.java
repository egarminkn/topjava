package ru.javawebinar.topjava.web.meal;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by eGarmin on 26.10.2016.
 */
public class LocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        return source.isEmpty() ? null : LocalTime.parse(source, DateTimeFormatter.ISO_TIME);
    }
}
