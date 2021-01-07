package querqy.model.builder.model;

import lombok.AllArgsConstructor;
import querqy.model.builder.TypeCastingBuilderUtils;
import querqy.model.builder.converter.MapValueConverter;
import querqy.model.builder.converter.MapValueConverterImpl.QueryNodeConverter;
import querqy.model.builder.converter.MapValueConverterImpl.ListOfQueryNodeConverter;

import java.util.Collections;

import static querqy.model.builder.model.Occur.SHOULD;

@AllArgsConstructor
public enum BuilderFieldProperties {
    CLAUSES("clauses", Collections.emptyList(), new ListOfQueryNodeConverter()),

    // TODO: write converter
    IS_GENERATED("is_generated", false, String::valueOf),
    OCCUR("occur", SHOULD, obj -> ((Occur) obj).typeName),

    VALUE("value"),
    FIELD("field"),

    QUERY("query", new QueryNodeConverter()),
    USER_QUERY("user_query", new QueryNodeConverter()),
    FILTER_QUERIES("filter_queries", Collections.emptyList(), new ListOfQueryNodeConverter()),
    BOOST_UP_QUERIES("boost_up_queries", Collections.emptyList(), new ListOfQueryNodeConverter()),
    BOOST_DOWN_QUERIES("boost_down_queries", Collections.emptyList(), new ListOfQueryNodeConverter()),

    // TODO: write converter
    BOOST("boost", 1.0f, obj -> TypeCastingBuilderUtils.castFloatOrDoubleToFloat(obj).get());

    public final String fieldName;
    public final Object defaultValue;
    public final MapValueConverter mapValueConverter;

    BuilderFieldProperties(final String fieldName) {
        this(fieldName, null, obj -> obj);
    }

    BuilderFieldProperties(final String fieldName, final Object defaultValue) {
        this(fieldName, defaultValue, obj -> obj);
    }

    BuilderFieldProperties(final String fieldName, final MapValueConverter mapValueConverter) {
        this(fieldName, null, mapValueConverter);
    }

    public boolean hasDefault() {
        return this.defaultValue != null;
    }

}
