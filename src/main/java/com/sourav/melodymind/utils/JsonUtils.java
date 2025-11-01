package com.sourav.melodymind.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JSON utility methods for MelodyMind application.
 * Provides common JSON parsing and serialization functions.
 * 
 * @author MelodyMind Team
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public final class JsonUtils {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    // Private constructor to prevent instantiation
    private JsonUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Converts an object to JSON string.
     * 
     * @param object the object to convert
     * @return JSON string or null if conversion fails
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Converts an object to pretty-printed JSON string.
     * 
     * @param object the object to convert
     * @return pretty JSON string or null if conversion fails
     */
    public static String toPrettyJson(Object object) {
        if (object == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to pretty JSON: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Parses JSON string to specified class.
     * 
     * @param json the JSON string
     * @param clazz the target class
     * @param <T> the type parameter
     * @return parsed object or null if parsing fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || clazz == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to {}: {}", clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }
    
    /**
     * Parses JSON string to specified type reference.
     * 
     * @param json the JSON string
     * @param typeReference the type reference
     * @param <T> the type parameter
     * @return parsed object or null if parsing fails
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json) || typeReference == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to type reference: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Parses JSON string to JsonNode.
     * 
     * @param json the JSON string
     * @return JsonNode or null if parsing fails
     */
    public static JsonNode parseToNode(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to JsonNode: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Safely gets a string value from JsonNode.
     * 
     * @param node the JsonNode
     * @param fieldName the field name
     * @return string value or null if not found
     */
    public static String getStringValue(JsonNode node, String fieldName) {
        if (node == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        
        JsonNode fieldNode = node.path(fieldName);
        return fieldNode.isMissingNode() ? null : fieldNode.asText();
    }
    
    /**
     * Safely gets an integer value from JsonNode.
     * 
     * @param node the JsonNode
     * @param fieldName the field name
     * @return integer value or null if not found
     */
    public static Integer getIntValue(JsonNode node, String fieldName) {
        if (node == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        
        JsonNode fieldNode = node.path(fieldName);
        return fieldNode.isMissingNode() ? null : fieldNode.asInt();
    }
    
    /**
     * Safely gets a double value from JsonNode.
     * 
     * @param node the JsonNode
     * @param fieldName the field name
     * @return double value or null if not found
     */
    public static Double getDoubleValue(JsonNode node, String fieldName) {
        if (node == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        
        JsonNode fieldNode = node.path(fieldName);
        return fieldNode.isMissingNode() ? null : fieldNode.asDouble();
    }
    
    /**
     * Safely gets a boolean value from JsonNode.
     * 
     * @param node the JsonNode
     * @param fieldName the field name
     * @return boolean value or null if not found
     */
    public static Boolean getBooleanValue(JsonNode node, String fieldName) {
        if (node == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        
        JsonNode fieldNode = node.path(fieldName);
        return fieldNode.isMissingNode() ? null : fieldNode.asBoolean();
    }
    
    /**
     * Checks if a JSON string is valid.
     * 
     * @param json the JSON string to validate
     * @return true if valid JSON
     */
    public static boolean isValidJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
    
    /**
     * Converts JSON string to Map.
     * 
     * @param json the JSON string
     * @return Map representation or empty map if conversion fails
     */
    public static Map<String, Object> toMap(String json) {
        if (StringUtils.isBlank(json)) {
            return Map.of();
        }
        
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to Map: {}", e.getMessage());
            return Map.of();
        }
    }
    
    /**
     * Converts JSON string to List.
     * 
     * @param json the JSON string
     * @return List representation or empty list if conversion fails
     */
    public static List<Object> toList(String json) {
        if (StringUtils.isBlank(json)) {
            return List.of();
        }
        
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to List: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Merges two JSON objects.
     * 
     * @param json1 the first JSON string
     * @param json2 the second JSON string
     * @return merged JSON string or null if merge fails
     */
    public static String mergeJson(String json1, String json2) {
        if (StringUtils.isBlank(json1)) {
            return json2;
        }
        if (StringUtils.isBlank(json2)) {
            return json1;
        }
        
        try {
            JsonNode node1 = OBJECT_MAPPER.readTree(json1);
            JsonNode node2 = OBJECT_MAPPER.readTree(json2);
            
            JsonNode merged = merge(node1, node2);
            return OBJECT_MAPPER.writeValueAsString(merged);
        } catch (JsonProcessingException e) {
            log.error("Error merging JSON: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Safely extracts a nested value from JSON.
     * 
     * @param json the JSON string
     * @param path the dot-separated path (e.g., "user.profile.name")
     * @return extracted value or empty Optional if not found
     */
    public static Optional<String> extractNestedValue(String json, String path) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(path)) {
            return Optional.empty();
        }
        
        try {
            JsonNode rootNode = OBJECT_MAPPER.readTree(json);
            String[] pathParts = path.split("\\.");
            
            JsonNode currentNode = rootNode;
            for (String part : pathParts) {
                currentNode = currentNode.path(part);
                if (currentNode.isMissingNode()) {
                    return Optional.empty();
                }
            }
            
            return Optional.of(currentNode.asText());
        } catch (JsonProcessingException e) {
            log.error("Error extracting nested value from JSON: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Merges two JsonNodes recursively.
     * 
     * @param mainNode the main node
     * @param updateNode the update node
     * @return merged JsonNode
     */
    private static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        if (updateNode == null) {
            return mainNode;
        }
        
        if (mainNode == null) {
            return updateNode;
        }
        
        if (mainNode.isObject() && updateNode.isObject()) {
            updateNode.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                JsonNode updateValue = entry.getValue();
                JsonNode mainValue = mainNode.get(fieldName);
                
                if (mainValue != null && mainValue.isObject() && updateValue.isObject()) {
                    merge(mainValue, updateValue);
                } else {
                    ((com.fasterxml.jackson.databind.node.ObjectNode) mainNode).set(fieldName, updateValue);
                }
            });
        }
        
        return mainNode;
    }
}