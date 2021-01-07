package querqy.model.builder;

import querqy.model.builder.model.BuilderFieldType;
import querqy.model.builder.model.QueryBuilderMap;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.BuilderFieldProperties;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static querqy.model.builder.TypeCastingBuilderUtils.createCastFunctionForInterface;

public interface QueryNodeBuilder<B, O, P> {

    B getBuilder();

    Class<B> getBuilderClass();

    String getNameOfQueryType();

    default void checkAndSetDefaults() {
        final Field[] fields = getBuilderClass().getDeclaredFields();

        for (final Field field : fields) {
            field.setAccessible(true);

            final BuilderField fieldAnnotation = field.getAnnotation(BuilderField.class);

            if (nonNull(fieldAnnotation)) {
                try {
                    final Object fieldValue = field.get(getBuilder());
                    if (isNull(fieldValue)) {
                        final Object defaulValue = fieldAnnotation.fieldProperties().defaultValue;

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
        checkAndSetDefaults();

        final QueryBuilderMap queryBuilderMap = new QueryBuilderMap();

        for (final Field field : getBuilderClass().getDeclaredFields()) {
            field.setAccessible(true);

            final BuilderField fieldAnnotation = field.getAnnotation(BuilderField.class);

            if (nonNull(fieldAnnotation)) {
                final BuilderFieldProperties properties = fieldAnnotation.fieldProperties();

                try {
                    final Object fieldValue = field.get(getBuilder());
                    if (nonNull(fieldValue)) {
                        queryBuilderMap.put(properties.fieldName, properties.mapValueConverter.toMapValue(fieldValue));
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
