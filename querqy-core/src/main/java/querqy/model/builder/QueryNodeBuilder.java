package querqy.model.builder;

import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.BuilderFieldProperties;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public interface QueryNodeBuilder<B, O, P> {

    default B getBuilder() {
        return null;
    }

    default Class<?> getBuilderClass() {
        return Object.class;
    }

    String getNameOfQueryType();


    @Deprecated
    void setDefaults();

    default void checkAndSetDefaults() {
        final Field[] fields = getBuilderClass().getDeclaredFields();

        for (final Field field : fields) {
            field.setAccessible(true);

            final BuilderField fieldAnnotation = field.getAnnotation(BuilderField.class);

            if (nonNull(fieldAnnotation)) {
                final BuilderFieldProperties properties = fieldAnnotation.fieldProperties();

                try {
                    final Object fieldValue = field.get(getBuilder());
                    if (isNull(fieldValue)) {
                        final Object defaulValue = properties.defaultValue;

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

    // TODO: change this that checks are automatically done when something is built
    O build(final P parent);

    B setAttributesFromObject(final O o);


    default Map<String, Object> toMap() {
        setDefaults();
        return Collections.singletonMap(getNameOfQueryType(), attributesToMap());
    }
    Map<String, Object> attributesToMap();

    default B setAttributesFromWrappedMap(final Map map) {
        final Object attributes = map.get(getNameOfQueryType());

        if (attributes instanceof Map) {
            return setAttributesFromMap((Map) attributes);

        } else {
            throw new QueryBuilderException(String.format("Attributes are expected to be wrapped by %s", getNameOfQueryType()));
        }
    }

    B setAttributesFromMap(final Map map);


}
