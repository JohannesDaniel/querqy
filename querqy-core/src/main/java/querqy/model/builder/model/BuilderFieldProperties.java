package querqy.model.builder.model;

import lombok.AllArgsConstructor;
import querqy.model.builder.converter.MapValueConverter;

import java.util.Collections;

import static querqy.model.builder.converter.MapValueConverterImpl.FLOAT_CONVERTER;
import static querqy.model.builder.converter.MapValueConverterImpl.LIST_OF_QUERY_NODE_CONVERTER;
import static querqy.model.builder.converter.MapValueConverterImpl.OCCUR_CONVERTER;
import static querqy.model.builder.converter.MapValueConverterImpl.QUERY_NODE_CONVERTER;
import static querqy.model.builder.model.Occur.SHOULD;

@AllArgsConstructor
public enum BuilderFieldProperties {

    // TODO: write converter -> this should be the default converter, the string conversion should happen in QueryBuilderMap
    IS_GENERATED("is_generated", false, String::valueOf),
    OCCUR("occur", SHOULD, OCCUR_CONVERTER),

    VALUE("value"),
    FIELD("field"),

    QUERY("query", QUERY_NODE_CONVERTER),
    USER_QUERY("user_query", QUERY_NODE_CONVERTER),

    CLAUSES("clauses", Collections.emptyList(), LIST_OF_QUERY_NODE_CONVERTER),
    FILTER_QUERIES("filter_queries", Collections.emptyList(), LIST_OF_QUERY_NODE_CONVERTER),
    BOOST_UP_QUERIES("boost_up_queries", Collections.emptyList(), LIST_OF_QUERY_NODE_CONVERTER),
    BOOST_DOWN_QUERIES("boost_down_queries", Collections.emptyList(), LIST_OF_QUERY_NODE_CONVERTER),

    BOOST("boost", 1.0f, FLOAT_CONVERTER);

    public final String fieldName;
    public final Object defaultValue;
    public final MapValueConverter mapValueConverter;

    BuilderFieldProperties(final String fieldName) {
        this(fieldName, null, obj -> obj);
    }

    BuilderFieldProperties(final String fieldName, final MapValueConverter mapValueConverter) {
        this(fieldName, null, mapValueConverter);
    }

    public boolean hasDefault() {
        return this.defaultValue != null;
    }

}
