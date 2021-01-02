package querqy.model.builder;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import querqy.ComparableCharSequenceWrapper;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.Node;
import querqy.model.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class QueryBuilder {

    private final List<DisjunctionMaxQueryBuilder> clauses;

    private QueryBuilder(final List<DisjunctionMaxQueryBuilder> clauses) {
        this.clauses = clauses;
    }

    public QueryBuilder setParent(final Node parent) {
        throw new UnsupportedOperationException("Not allowed to set parent Node for QueryBuilder");
    }

    public QueryBuilder addDmqBuilder(final DisjunctionMaxQueryBuilder builder) {
        this.clauses.add(builder);
        return this;
    }

    public List<DisjunctionMaxQueryBuilder> getClauses() {
        return clauses;
    }

    public Query build() {
//        final Query query = new Query();
//        clauses.stream().map(dmq -> dmq.setParent(query).build()).forEach(query::addClause);
//        return query;
        return null;
    }

    public Map<String, Object> toMap() {
        return null;

    }

    public static QueryBuilder builder() {
        return new QueryBuilder(new ArrayList<>());
    }

    public static QueryBuilder fromQuery(final Query query) {
        QueryBuilder builder = builder();

        query.getClauses().stream()
                .map(clause -> {

                    if (clause instanceof DisjunctionMaxQuery) {
                        return DisjunctionMaxQueryBuilder.fromQuery((DisjunctionMaxQuery) clause);

                    } else {
                        throw new UnsupportedOperationException("The structure of this query is currently not supported by builders");
                    }})

                .forEach(builder::addDmqBuilder);

        return builder;
    }

    public static QueryBuilder query() {
        return new QueryBuilder(Collections.emptyList());
    }

    public static QueryBuilder query(final DisjunctionMaxQueryBuilder... dmqs) {
        return new QueryBuilder(Arrays.stream(dmqs).collect(Collectors.toList()));
    }

    public static QueryBuilder query(final String... terms) {
        return new QueryBuilder(Arrays.stream(terms)
                .map(ComparableCharSequenceWrapper::new)
                .map(DisjunctionMaxQueryBuilder::dmq)
                .collect(Collectors.toList()));
    }

    @JsonGetter("clauses")
    //@JsonValue
    public List<Map<String, DisjunctionMaxQueryBuilder>> serializeClauses() {
        return this.clauses.stream()
                .map(clause -> Collections.singletonMap("dmq", clause))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return clauses.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "query[", "]")) ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryBuilder builder = (QueryBuilder) o;
        return Objects.equals(clauses, builder.clauses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clauses);
    }
}
