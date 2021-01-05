package querqy.model.builder.model;

import lombok.AllArgsConstructor;
import querqy.model.builder.BuilderUtils;

import java.util.Collections;

import static querqy.model.builder.model.BuilderToMapValueResolver.convertListOfQueryNodeBuildersToMap;
import static querqy.model.builder.model.Occur.SHOULD;

@AllArgsConstructor
public enum BuilderFieldProperties {
    CLAUSES("clauses", Collections.emptyList(), convertListOfQueryNodeBuildersToMap),
    IS_GENERATED("is_generated", false, String::valueOf),
    OCCUR("occur", SHOULD, obj -> ((Occur) obj).typeName),

    VALUE("value", null, obj -> obj),
    FIELD("field", null, obj -> obj),

    QUERY("query", null, null),
    USER_QUERY("user_query", null, null),
    FILTER_QUERIES("filter_queries", Collections.emptyList(), null),
    BOOST_UP_QUERIES("boost_up_queries", Collections.emptyList(), null),
    BOOST_DOWN_QUERIES("boost_down_queries", Collections.emptyList(), null),

    BOOST("boost", 1.0f, null);

    public final String fieldName;
    public final Object defaultValue;
    public final BuilderToMapValueResolver builderToMapValueResolver;



    public boolean hasDefault() {
        return this.defaultValue != null;
    }

}
