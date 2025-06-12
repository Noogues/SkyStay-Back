package com.example.skystayback.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeParser {

    public static LocalDateTime parseDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        return LocalDateTime.parse(dateTime, formatter);
    }
}
