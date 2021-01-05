package querqy.model.builder.impl;

import java.util.LinkedHashMap;

import static java.util.Objects.nonNull;

public class QueryBuilderMap extends LinkedHashMap<String, Object> {

    public void putIfNotNull(final String key, final Object value) {
        if (nonNull(value)) {
            super.put(key, value);
        }
    }

    public void putBooleanAsString(final String key, final boolean value) {
        super.put(key, String.valueOf(value));
    }

}
