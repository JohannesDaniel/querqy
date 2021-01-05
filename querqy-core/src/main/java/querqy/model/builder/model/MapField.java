package querqy.model.builder.model;

import lombok.AllArgsConstructor;

import java.util.Collections;

import static querqy.model.builder.model.Occur.SHOULD;

@AllArgsConstructor
public enum MapField {
    CLAUSES("clauses", Collections.emptyList()),
    IS_GENERATED("is_generated", false),
    OCCUR("occur", SHOULD),

    VALUE("value", null),
    FIELD("field", null),

    QUERY("query", null),
    USER_QUERY("user_query", null),
    FILTER_QUERIES("filter_queries", Collections.emptyList()),
    BOOST_UP_QUERIES("boost_up_queries", Collections.emptyList()),
    BOOST_DOWN_QUERIES("boost_down_queries", Collections.emptyList()),

    BOOST("boost", null);

    public final String fieldName;
    public final Object defaultValue;

    public boolean hasDefault() {
        return this.defaultValue != null;
    }

}
