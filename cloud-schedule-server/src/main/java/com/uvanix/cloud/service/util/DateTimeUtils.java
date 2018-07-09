package com.uvanix.cloud.service.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author uavnix
 * @date 2018/7/7
 */
public final class DateTimeUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private DateTimeUtils() {
    }

    public static String toString(Date date) {
        return utilDateToLocalDateTime(date).format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    public static String toString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
    }

    public static String toString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    public static long nowOfLong() {
        return System.currentTimeMillis() / 1000;
    }

    public static int nowOfInteger() {
        return (int) nowOfLong();
    }

    public static Date nowOfDate() {
        return localDateTimeToUtilDate(LocalDateTime.now());
    }

    public static Date parseStringToDate(String text, String pattern) {
        if (text.length() > 10) {
            LocalDateTime localDateTime = LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
            return localDateTimeToUtilDate(localDateTime);
        }

        LocalDate localDate = LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
        return localDateToUtilDate(localDate);
    }

    public static Date intTimeToUtilDate(long time) {
        return new Date(time * 1000);
    }

    public static LocalDate intTimeToLocalDate(long time) {
        return utilDateToLocalDate(intTimeToUtilDate(time));
    }

    public static LocalDateTime intTimeToLocalDateTime(long time) {
        return utilDateToLocalDateTime(intTimeToUtilDate(time));
    }

    public static LocalTime intTimeToLocalTime(long time) {
        return utilDateToLocalTime(intTimeToUtilDate(time));
    }

    public static Date localDateTimeToUtilDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date localDateToUtilDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date localTimeToUtilDate(LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), localTime);
        return localDateTimeToUtilDate(localDateTime);
    }

    public static LocalDateTime utilDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDate utilDateToLocalDate(Date date) {
        return utilDateToLocalDateTime(date).toLocalDate();
    }

    public static LocalTime utilDateToLocalTime(Date date) {
        return utilDateToLocalDateTime(date).toLocalTime();
    }
}
