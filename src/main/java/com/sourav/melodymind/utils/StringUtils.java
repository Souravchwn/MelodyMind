package com.sourav.melodymind.utils;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * String utility methods for MelodyMind application.
 * Provides common string manipulation and validation functions.
 * 
 * @author MelodyMind Team
 * @version 1.0
 * @since 1.0
 */
public final class StringUtils {
    
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9\\s]");
    private static final Pattern DIACRITICS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    
    // Private constructor to prevent instantiation
    private StringUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Checks if a string is null or empty.
     * 
     * @param str the string to check
     * @return true if the string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    /**
     * Checks if a string is null, empty, or contains only whitespace.
     * 
     * @param str the string to check
     * @return true if the string is null, empty, or blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Checks if a string is not null and not empty.
     * 
     * @param str the string to check
     * @return true if the string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Checks if a string is not null, not empty, and contains non-whitespace characters.
     * 
     * @param str the string to check
     * @return true if the string is not null, not empty, and not blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    /**
     * Returns the string if it's not null, otherwise returns an empty string.
     * 
     * @param str the string to check
     * @return the string or empty string if null
     */
    public static String defaultIfNull(String str) {
        return str != null ? str : "";
    }
    
    /**
     * Returns the string if it's not null, otherwise returns the default value.
     * 
     * @param str the string to check
     * @param defaultValue the default value to return if str is null
     * @return the string or default value if null
     */
    public static String defaultIfNull(String str, String defaultValue) {
        return str != null ? str : defaultValue;
    }
    
    /**
     * Normalizes a string for search purposes by removing diacritics,
     * converting to lowercase, and removing extra whitespace.
     * 
     * @param str the string to normalize
     * @return normalized string
     */
    public static String normalize(String str) {
        if (isEmpty(str)) {
            return "";
        }
        
        // Remove diacritics (accents)
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        normalized = DIACRITICS_PATTERN.matcher(normalized).replaceAll("");
        
        // Convert to lowercase and trim
        normalized = normalized.toLowerCase().trim();
        
        // Replace multiple whitespaces with single space
        normalized = WHITESPACE_PATTERN.matcher(normalized).replaceAll(" ");
        
        return normalized;
    }
    
    /**
     * Normalizes a string for use as a cache key or identifier.
     * Removes special characters and normalizes whitespace.
     * 
     * @param str the string to normalize
     * @return normalized string suitable for use as a key
     */
    public static String normalizeForKey(String str) {
        if (isEmpty(str)) {
            return "";
        }
        
        String normalized = normalize(str);
        
        // Remove special characters except spaces
        normalized = SPECIAL_CHARS_PATTERN.matcher(normalized).replaceAll("");
        
        // Replace spaces with underscores
        normalized = normalized.replace(" ", "_");
        
        return normalized;
    }
    
    /**
     * Capitalizes the first letter of each word in a string.
     * 
     * @param str the string to capitalize
     * @return capitalized string
     */
    public static String capitalizeWords(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return Arrays.stream(str.split("\\s+"))
                .map(word -> word.isEmpty() ? word : 
                     Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
    
    /**
     * Truncates a string to the specified length, adding ellipsis if truncated.
     * 
     * @param str the string to truncate
     * @param maxLength the maximum length
     * @return truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Truncates a string to the specified length with custom suffix.
     * 
     * @param str the string to truncate
     * @param maxLength the maximum length
     * @param suffix the suffix to add if truncated
     * @return truncated string
     */
    public static String truncate(String str, int maxLength, String suffix) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        
        String safeSuffix = defaultIfNull(suffix, "");
        int truncateLength = Math.max(0, maxLength - safeSuffix.length());
        
        return str.substring(0, truncateLength) + safeSuffix;
    }
    
    /**
     * Joins a list of strings with the specified delimiter.
     * 
     * @param strings the list of strings to join
     * @param delimiter the delimiter to use
     * @return joined string
     */
    public static String join(List<String> strings, String delimiter) {
        if (strings == null || strings.isEmpty()) {
            return "";
        }
        
        return strings.stream()
                .filter(StringUtils::isNotNull)
                .collect(Collectors.joining(defaultIfNull(delimiter, "")));
    }
    
    /**
     * Splits a string by the specified delimiter and returns a list.
     * 
     * @param str the string to split
     * @param delimiter the delimiter to split by
     * @return list of split strings
     */
    public static List<String> split(String str, String delimiter) {
        if (isEmpty(str)) {
            return List.of();
        }
        
        return Arrays.stream(str.split(Pattern.quote(defaultIfNull(delimiter, ","))))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
    }
    
    /**
     * Checks if a string matches the given regex pattern.
     * 
     * @param str the string to check
     * @param regex the regex pattern
     * @return true if the string matches the pattern
     */
    public static boolean matches(String str, String regex) {
        if (isEmpty(str) || isEmpty(regex)) {
            return false;
        }
        
        return Pattern.matches(regex, str);
    }
    
    /**
     * Removes all whitespace from a string.
     * 
     * @param str the string to process
     * @return string without whitespace
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return str.replaceAll("\\s", "");
    }
    
    /**
     * Counts the number of occurrences of a substring in a string.
     * 
     * @param str the string to search in
     * @param substring the substring to count
     * @return number of occurrences
     */
    public static int countOccurrences(String str, String substring) {
        if (isEmpty(str) || isEmpty(substring)) {
            return 0;
        }
        
        int count = 0;
        int index = 0;
        
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        
        return count;
    }
    
    /**
     * Checks if a string is not null.
     * 
     * @param str the string to check
     * @return true if the string is not null
     */
    private static boolean isNotNull(String str) {
        return str != null;
    }
    
    /**
     * Generates a cache key from artist and title.
     * 
     * @param artist the artist name
     * @param title the song title
     * @return normalized cache key
     */
    public static String generateCacheKey(String artist, String title) {
        String normalizedArtist = normalizeForKey(artist);
        String normalizedTitle = normalizeForKey(title);
        return normalizedArtist + "_" + normalizedTitle;
    }
    
    /**
     * Sanitizes a string for safe display in HTML.
     * 
     * @param str the string to sanitize
     * @return sanitized string
     */
    public static String sanitizeForHtml(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#x27;");
    }
}