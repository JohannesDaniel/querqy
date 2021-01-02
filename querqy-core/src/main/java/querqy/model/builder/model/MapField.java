package querqy.model.builder.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MapField {
    CLAUSES("clauses"),
    IS_GENERATED("is_generated"),
    OCCUR("occur"),

    VALUE("value"),
    FIELD("field");

    public final String fieldName;
}
