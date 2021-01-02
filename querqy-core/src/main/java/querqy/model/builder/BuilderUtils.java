package querqy.model.builder;

import querqy.model.builder.model.Occur;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static querqy.model.builder.model.Occur.getOccurByTypeName;

public class BuilderUtils {

    private BuilderUtils() {}

    public static Optional<Map> getMapForExpectedKey(final String expectedKey, final Map map) {
        return castMap(map.get(expectedKey));
    }

    // public static Object expectMapToContainExactlyOneEntry(final Map map, final String expectedKey) throws QueryBuilderException {

    public static String expectMapToContainExactlyOneEntryAndGetKey(final Map map) {
        if (map.size() == 1) {
            return map.keySet().iterator().next().toString();
        }

        throw new QueryBuilderException(String.format("Map is expected to contain exactly one element: %s", map.toString()));
    }

    public static Optional<Occur> castOccur(final Object obj) {
        final Optional<String> optionalOccurName = castString(obj);

        if (optionalOccurName.isPresent()) {
            final Occur occur = getOccurByTypeName(optionalOccurName.get());
            return Optional.of(occur);

        } else {
            return Optional.empty();
        }
    }

    public static Optional<Map> castMap(final Object obj) {
        if (obj instanceof Map) {
            return Optional.of((Map) obj);
        } else if (isNull(obj)) {
            return Optional.empty();

        } else {
            throw new QueryBuilderException(String.format("Element %s is expected to be of type Map", obj.toString()));
        }
    }

    public static Optional<List> castList(final Object obj) {
        if (obj instanceof List) {
            return Optional.of((List) obj);
        } else if (isNull(obj)) {
            return Optional.empty();

        } else {
            throw new QueryBuilderException(String.format("Element %s is expected to be of type List", obj.toString()));
        }
    }

    public static Optional<String> castString(final Object obj) {
        if (obj instanceof String) {
            return Optional.of((String) obj);

        } else if (isNull(obj)) {
            return Optional.empty();

        } else {
            throw new QueryBuilderException(String.format("Element %s is expected to be of type String", obj.toString()));
        }
    }

    public static Optional<Boolean> castStringOrBooleanToBoolean(final Object obj) {
        if (obj instanceof Boolean) {
            return Optional.of((Boolean) obj);

        } else if (obj instanceof String) {
            return Optional.of(Boolean.valueOf((String) obj));

        } else if (isNull(obj)) {
            return Optional.empty();

        } else {
            throw new QueryBuilderException(String.format("Element %s is expected to be of type String or Boolean", obj.toString()));
        }
    }

    public static class QueryBuilderMap extends LinkedHashMap<String, Object> {

        public void putIfNotNull(final String key, final Object value) {
            if (nonNull(value)) {
                super.put(key, value);
            }
        }

        public void putBooleanAsString(final String key, final boolean value) {
            super.put(key, String.valueOf(value));
        }

    }
}
