package util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonSerializer {
    public String serialize(Object object) throws IllegalAccessException {
        Map<String, Object> jsonElements = new HashMap<>();

        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {

            if (field.isAnnotationPresent(JsonField.class)) {
                field.setAccessible(true); // Permite acessar campos privados

                JsonField jsonFieldAnnotation = field.getAnnotation(JsonField.class);

                String jsonKey = jsonFieldAnnotation.value();

                jsonElements.put(jsonKey, field.get(object));
            }
        }

        String jsonString = jsonElements.entrySet()
                .stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));

        return "{" + jsonString + "}";
    }
}
