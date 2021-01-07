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
        // return Collections.singletonMap(getNameOfQueryType(), attributesToMap());
    }

    @Deprecated
    Map<String, Object> attributesToMap();

    // default


    default B fromMap(final Map map) {

        final List<BuilderFieldType> builderFieldTypes = new ArrayList<>();

        for (final Field field : getBuilderClass().getDeclaredFields()) {
            field.setAccessible(true);

                /*
                List<Class>
                List<Interface>

                String, Occur, Boolean, Interface

                Interfaces: QuerqyQueryBuilder, DisjunctionMaxClauseBuilder
                 */
            final BuilderField fieldAnnotation = field.getAnnotation(BuilderField.class);

            if (nonNull(fieldAnnotation)) {

                final Type type = field.getGenericType();

                if (type instanceof ParameterizedType) {
                    final Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();

                    if (actualTypes.length != 1) {
                        throw new QueryBuilderException(String.format(
                                "The use of generics for field %s is currently not supported by builders", field.getName()));
                    }

                    if (actualTypes[0] instanceof Class<?>) {
                        final Class<?> clazz = (Class<?>) actualTypes[0];

                        if (clazz.isInterface()) {
                            System.out.println("parametrized interface   " + clazz.getName());
                            createCastFunctionForInterface(clazz);
                        } else {
                            System.out.println("parametrized class   " + clazz.getName());

                        }

                    } else {
                        throw new QueryBuilderException(String.format(
                                "An unexpected error occurred parsing fields for class %s", getBuilderClass().getName()));
                    }

                } else {
                    if (type instanceof Class<?>) {
                        final Class<?> clazz = (Class<?>) type;

                        if (clazz.isInterface()) {
                            System.out.println("interface   " + clazz.getName());
                        } else {
                            System.out.println("class   " + clazz.getName());

                        }
                    } else {
                        throw new QueryBuilderException(String.format(
                                "An unexpected error occurred parsing fields for class %s", getBuilderClass().getName()));
                    }
                }


                // final Class<?> clazz = (Class<?>) type;
                // if (type.getClass().isInterface())
            }


        }




        // get field
        // get type
        // parameterized?
        // raw type?
        // interface?

        // get casted value for class type
        // raw should be easy
        // resolve interface
        // resolve list


//            for (final Field field : getBuilderClass().getFields()) {
//                field.setAccessible(true);
//
//                System.out.println(field.getName());
//
//            }
//
//
//            return null;


        return null;
    }

    default B setAttributesFromWrappedMap(final Map map) {
        final Object rawAttributes = map.get(getNameOfQueryType());

        if (rawAttributes instanceof Map) {
            return setAttributesFromMap((Map) rawAttributes);
        } else {
            throw new QueryBuilderException(String.format("Attributes are expected to be wrapped by %s", getNameOfQueryType()));
        }
    }

    B setAttributesFromMap(final Map map);


}
