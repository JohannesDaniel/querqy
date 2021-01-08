package querqy.model.builder;

import querqy.model.builder.converter.MapSettings;
import querqy.model.builder.converter.QueryBuilderMap;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.BuilderFieldSettings;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public interface QueryNodeBuilder<B, O, P> {

    B getBuilder();

    Class<B> getBuilderClass();

    String getNameOfQueryType();

    default B checkAndSetDefaults() {
        final Field[] fields = getBuilderClass().getDeclaredFields();

        for (final Field field : fields) {
            field.setAccessible(true);

            final BuilderField fieldAnnotation = field.getAnnotation(BuilderField.class);

            if (nonNull(fieldAnnotation)) {
                try {
                    final Object fieldValue = field.get(getBuilder());
                    if (isNull(fieldValue)) {
                        final Object defaulValue = fieldAnnotation.settings().defaultValue;

                        if (isNull(defaulValue) && fieldAnnotation.fieldIsMandatory()) {
                            throw new QueryBuilderException(
                                    String.format("Field %s is mandatory for builder %s",
                                            field.getName(), getNameOfQueryType()));
                        }

                        field.set(getBuilder(), defaulValue);
                    }

                } catch (IllegalAccessException e) {
                    throw new QueryBuilderException(
                            String.format("Error happened when setting defaults for field %s", field.getName()), e);
                }
            }
        }
        return getBuilder();
    }

    default O build() {
        checkAndSetDefaults();
        return build(null);
    }

    default O build(final P parent) {
        checkAndSetDefaults();
        return buildObject(parent);
    }

    O buildObject(final P parent);

    B setAttributesFromObject(final O o);

    default Map<String, Object> toMap() {
        return toMap(MapSettings.defaults());
    }

    default Map<String, Object> toMap(final MapSettings mapSettings) {
        checkAndSetDefaults();

        final QueryBuilderMap queryBuilderMap = new QueryBuilderMap();

        for (final Field field : getBuilderClass().getDeclaredFields()) {
            field.setAccessible(true);

            final BuilderField fieldAnnotation = field.getAnnotation(BuilderField.class);

            if (nonNull(fieldAnnotation)) {
                final BuilderFieldSettings settings = fieldAnnotation.settings();

                try {
                    final Object fieldValue = field.get(getBuilder());
                    if (nonNull(fieldValue)) {
                        queryBuilderMap.putBuilderField(settings, fieldValue, mapSettings);
                        // queryBuilderMap.put(settings.fieldName, settings.mapValueConverter.toMapValue(fieldValue));
                    }

                } catch (IllegalAccessException e) {
                    throw new QueryBuilderException(
                            String.format("Error happened when setting defaults for field %s", field.getName()), e);
                }
            }
        }

        return Collections.singletonMap(getNameOfQueryType(), queryBuilderMap);
    }

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
