package com.github.systemdir.gml.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Creates a unique integer-based id for each object passed to the function.
 * <p>
 * Uniqueness of an object is determined by the equals, and hashCode funktion of the passed objects.
 * <p>
 * This class is NOT thread safe.
 *
 * @author Andreas Hofstadler, COREFT
 */
public class UniqueIntIdFunction<T> implements Function<T, String> {
    private Map<T, String> idMap = new HashMap<>();
    private int nextId = 1;

    @Override
    public String apply(T t) {
        String existing = idMap.get(t);
        if (existing != null) {
            return existing;
        } else {
            String id = String.valueOf(nextId);
            nextId++;
            idMap.put(t, id);
            return id;
        }
    }
}
