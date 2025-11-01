package com.sourav.melodymind.utils;

import com.sourav.melodymind.constants.ApplicationConstants;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Date and time utility methods for MelodyMind application.
 * Provides common date/time manipulation and formatting functions.
 * 
 * @author MelodyMind Team
 * @version 1.0
 * @since 1.0
 */
public final class DateTimeUtils {
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    // Private constructor to prevent instantiation
    private DateTimeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Gets the current date and time.
     * 
     * @return current LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
    
    /**
     * Gets the current date and time in UTC.
     * 
     * @return current LocalDateTime in UTC
     */
    public static LocalDateTime nowUtc() {
        return ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime();
    }
    
    /**
     * Formats a LocalDateTime to ISO string format.
     * 
     * @param dateTime the date time to format
     * @return formatted string
     */
    public static String formatToIso(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(ISO_FORMATTER);
    }
    
    /**
     * Formats a LocalDateTime to display string format.
     * 
     * @param dateTime the date time to format
     * @return formatted string (yyyy-MM-dd HH:mm:ss)
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DISPLAY_FORMATTER);
    }
    
    /**
     * Formats a LocalDateTime to date only string.
     * 
     * @param dateTime the date time to format
     * @return formatted date string (yyyy-MM-dd)
     */
    public static String formatDateOnly(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_ONLY_FORMATTER);
    }
    
    /**
     * Formats a LocalDateTime to time only string.
     * 
     * @param dateTime the date time to format
     * @return formatted time string (HH:mm:ss)
     */
    public static String formatTimeOnly(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(TIME_ONLY_FORMATTER);
    }
    
    /**
     * Parses an ISO string to LocalDateTime.
     * 
     * @param isoString the ISO string to parse
     * @return parsed LocalDateTime
     */
    public static LocalDateTime parseFromIso(String isoString) {
        if (StringUtils.isBlank(isoString)) {
            return null;
        }
        return LocalDateTime.parse(isoString, ISO_FORMATTER);
    }
    
    /**
     * Gets a LocalDateTime representing the start of today.
     * 
     * @return start of today (00:00:00)
     */
    public static LocalDateTime startOfToday() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    }
    
    /**
     * Gets a LocalDateTime representing the end of today.
     * 
     * @return end of today (23:59:59.999999999)
     */
    public static LocalDateTime endOfToday() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
    }
    
    /**
     * Gets a LocalDateTime representing the start of yesterday.
     * 
     * @return start of yesterday
     */
    public static LocalDateTime startOfYesterday() {
        return startOfToday().minusDays(1);
    }
    
    /**
     * Gets a LocalDateTime representing the start of tomorrow.
     * 
     * @return start of tomorrow
     */
    public static LocalDateTime startOfTomorrow() {
        return startOfToday().plusDays(1);
    }
    
    /**
     * Gets a LocalDateTime representing N days ago from now.
     * 
     * @param days number of days ago
     * @return LocalDateTime N days ago
     */
    public static LocalDateTime daysAgo(int days) {
        return LocalDateTime.now().minusDays(days);
    }
    
    /**
     * Gets a LocalDateTime representing N hours ago from now.
     * 
     * @param hours number of hours ago
     * @return LocalDateTime N hours ago
     */
    public static LocalDateTime hoursAgo(int hours) {
        return LocalDateTime.now().minusHours(hours);
    }
    
    /**
     * Gets a LocalDateTime representing N minutes ago from now.
     * 
     * @param minutes number of minutes ago
     * @return LocalDateTime N minutes ago
     */
    public static LocalDateTime minutesAgo(int minutes) {
        return LocalDateTime.now().minusMinutes(minutes);
    }
    
    /**
     * Checks if a date is today.
     * 
     * @param dateTime the date to check
     * @return true if the date is today
     */
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        
        LocalDateTime today = LocalDateTime.now();
        return dateTime.toLocalDate().equals(today.toLocalDate());
    }
    
    /**
     * Checks if a date is yesterday.
     * 
     * @param dateTime the date to check
     * @return true if the date is yesterday
     */
    public static boolean isYesterday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return dateTime.toLocalDate().equals(yesterday.toLocalDate());
    }
    
    /**
     * Checks if a date is in the past.
     * 
     * @param dateTime the date to check
     * @return true if the date is in the past
     */
    public static boolean isPast(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        
        return dateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Checks if a date is in the future.
     * 
     * @param dateTime the date to check
     * @return true if the date is in the future
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        
        return dateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Calculates the difference in days between two dates.
     * 
     * @param start the start date
     * @param end the end date
     * @return number of days between the dates
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        
        return ChronoUnit.DAYS.between(start, end);
    }
    
    /**
     * Calculates the difference in hours between two dates.
     * 
     * @param start the start date
     * @param end the end date
     * @return number of hours between the dates
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        
        return ChronoUnit.HOURS.between(start, end);
    }
    
    /**
     * Calculates the difference in minutes between two dates.
     * 
     * @param start the start date
     * @param end the end date
     * @return number of minutes between the dates
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        
        return ChronoUnit.MINUTES.between(start, end);
    }
    
    /**
     * Gets the cutoff date for data retention based on configured days.
     * 
     * @return cutoff date for data cleanup
     */
    public static LocalDateTime getDataRetentionCutoff() {
        return daysAgo(ApplicationConstants.DEFAULT_DATA_RETENTION_DAYS);
    }
    
    /**
     * Gets the cutoff date for cache expiration (1 hour ago).
     * 
     * @return cutoff date for cache cleanup
     */
    public static LocalDateTime getCacheExpirationCutoff() {
        return hoursAgo(1);
    }
    
    /**
     * Checks if a date is older than the specified number of days.
     * 
     * @param dateTime the date to check
     * @param days the number of days
     * @return true if the date is older than the specified days
     */
    public static boolean isOlderThan(LocalDateTime dateTime, int days) {
        if (dateTime == null) {
            return false;
        }
        
        return dateTime.isBefore(daysAgo(days));
    }
    
    /**
     * Checks if a date is within the last N hours.
     * 
     * @param dateTime the date to check
     * @param hours the number of hours
     * @return true if the date is within the last N hours
     */
    public static boolean isWithinLastHours(LocalDateTime dateTime, int hours) {
        if (dateTime == null) {
            return false;
        }
        
        return dateTime.isAfter(hoursAgo(hours));
    }
    
    /**
     * Gets a human-readable relative time string.
     * 
     * @param dateTime the date to format
     * @return relative time string (e.g., "2 hours ago", "yesterday")
     */
    public static String getRelativeTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "unknown";
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        if (dateTime.isAfter(now)) {
            return "in the future";
        }
        
        long minutes = minutesBetween(dateTime, now);
        
        if (minutes < 1) {
            return "just now";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        }
        
        long hours = hoursBetween(dateTime, now);
        if (hours < 24) {
            return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        }
        
        long days = daysBetween(dateTime, now);
        if (days == 1) {
            return "yesterday";
        } else if (days < 7) {
            return days + " days ago";
        } else if (days < 30) {
            long weeks = days / 7;
            return weeks + " week" + (weeks == 1 ? "" : "s") + " ago";
        } else {
            long months = days / 30;
            return months + " month" + (months == 1 ? "" : "s") + " ago";
        }
    }
}