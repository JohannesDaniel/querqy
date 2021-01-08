package querqy.model.builder;

import querqy.model.builder.converter.MapConverter;

import java.util.Collections;
import java.util.Map;

public interface QueryNodeBuilder<B extends QueryNodeBuilder, O, P> {

    B getQueryBuilder();

    Class<B> getBuilderClass();

    String getNameOfQueryType();

    B checkMandatoryFieldValues();

    default O build() {
        return build(null);
    }

    default O build(final P parent) {
        checkMandatoryFieldValues();
        return buildObject(parent);
    }

    O buildObject(final P parent);

    B setAttributesFromObject(final O o);

    // TODO: Change this to remove all annotation stuff
//    default Map<String, Object> toMap() {
//        return toMap(new MapConverter());
//    }

    default Map<String, Object> toMap() {
        return Collections.singletonMap(getNameOfQueryType(), attributesToMap(new MapConverter()));
    }

    default Map<String, Object> toMap(final MapConverter mapConverter) {
        checkMandatoryFieldValues();

        return Collections.singletonMap(getNameOfQueryType(), attributesToMap(mapConverter));
    }

    Map<String, Object> attributesToMap(final MapConverter mapConverter);

//    default Map<String, Object> toMap(final MapConverter mapConverter) {
//        checkMandatoryFieldValues();
//
//        final Map<String, Object> map = new LinkedHashMap<>();
//
//        for (final Field field : getBuilderClass().getDeclaredFields()) {
//            field.setAccessible(true);
//
//            final MapField fieldAnnotation = field.getAnnotation(MapField.class);
//
//            if (nonNull(fieldAnnotation)) {
//                final MapFieldSettings settings = fieldAnnotation.settings();
//
//                try {
//                    mapConverter.convertAndPut(map, field.get(getQueryBuilder()), settings);
//
//                } catch (IllegalAccessException e) {
//                    throw new QueryBuilderException(
//                            String.format("Error happened when setting defaults for field %s", field.getName()), e);
//                }
//            }
//        }
//
//        return Collections.singletonMap(getNameOfQueryType(), map);
//    }

    default B fromMap(final Map map) {
        final Object rawAttributes = map.get(getNameOfQueryType());

        if (rawAttributes instanceof Map) {
            return setAttributesFromMap((Map) rawAttributes);
        } else {
            throw new QueryBuilderException(String.format("Attributes are expected to be wrapped by %s", getNameOfQueryType()));
        }
    }

    B setAttributesFromMap(final Map map);
}
