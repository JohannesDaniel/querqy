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

    // TODO: could be renamed to TypeCastingUtils and the QueryBuilderMap could be put into its own class file
    private BuilderUtils() {}

    public static Optional<Float> castFloatOrDoubleToFloat(final Object obj) {
        if (obj instanceof Double) {
            return Optional.of(((Double) obj).floatValue());

        } else if (obj instanceof Float) {
            return Optional.of((Float) obj);

        } else if (isNull(obj)) {
            return Optional.empty();

        } else {
            throw new QueryBuilderException(String.format("Element %s is expected to be of type Float", obj.toString()));
        }
    }

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
}
