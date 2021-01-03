package querqy.model.builder.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MapField {
    CLAUSES("clauses"),
    IS_GENERATED("is_generated"),
    OCCUR("occur"),

    VALUE("value"),
    FIELD("field"),

    USER_QUERY("user_query"),
    FILTER_QUERIES("filter_queries"),
    BOOST_UP_QUERIES("boost_up_queries"),
    BOOST_DOWN_QUERIES("boost_down_queries");

    public final String fieldName;
}
