package querqy.model.builder;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ExpandedQueryBuilder {

    private final QueryBuilder userQuery;

    @JsonSetter("query")
    public void parseQuery(Map<String, Object> map) {
        System.out.println(map);
    }

    public static ExpandedQueryBuilder expanded(final QueryBuilder userQuery) {
        return new ExpandedQueryBuilder(userQuery);
    }

    public static ExpandedQueryBuilder expanded(
            final QueryBuilder userQuery,
            final List<?> boostUpQueries,
            final List<?> boostDownQueries,
            final List<?> filterQueries) {
        return null;

    }
}
