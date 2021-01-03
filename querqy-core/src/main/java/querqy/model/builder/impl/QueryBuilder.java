package querqy.model.builder.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.ComparableCharSequenceWrapper;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.QuerqyQuery;
import querqy.model.Query;
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.QueryNodeBuilder;
import querqy.model.builder.TopLevelNodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static querqy.model.builder.impl.DisjunctionMaxQueryBuilder.dmq;
import static querqy.model.builder.model.MapField.CLAUSES;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@Deprecated
public class QueryBuilder implements TopLevelNodeBuilder<QueryBuilder, Query>,
        QuerqyQueryBuilder<QueryBuilder, Query, Object> {

    public static final String BUILDER_NAME = "query";

    private List<DisjunctionMaxQueryBuilder> clauses;

    @Override
    public void setDefaults() {

    }

    @Override
    public Query build() {
        final Query query = new Query();
        clauses.stream().map(clause -> clause.build(query)).forEach(query::addClause);
        return query;
    }

    @Override
    public QuerqyQuery<?> buildQuerqyQuery() {
        return build();
    }

    @Override
    public QueryBuilder setAttributesFromObject(final Query query) {
        final List<DisjunctionMaxQueryBuilder> dmqClauses = query.getClauses().stream()
                .map(clause -> {

                    if (clause instanceof DisjunctionMaxQuery) {
                        return new DisjunctionMaxQueryBuilder((DisjunctionMaxQuery) clause);

                    } else {
                        throw new QueryBuilderException("The structure of this query is currently not supported by builders");
                    }})

                .collect(Collectors.toList());

        this.setClauses(dmqClauses);
        return this;
    }

    @Override
    public String getNameOfQueryType() {
        return BUILDER_NAME;
    }

    @Override
    public Map attributesToMap() {
        final BuilderUtils.QueryBuilderMap map = new BuilderUtils.QueryBuilderMap();
        map.put(CLAUSES.fieldName, clauses.stream().map(QueryNodeBuilder::toMap).collect(Collectors.toList()));
        return map;
    }

    @Override
    public QueryBuilder setAttributesFromMap(final Map map) {
        final List<DisjunctionMaxQueryBuilder> parsedClauses = new ArrayList<>();

        final List rawClauses = BuilderUtils.castList(map.get(CLAUSES.fieldName)).orElse(Collections.emptyList());
        for (final Object rawClause : rawClauses) {
            final Optional<Map> optionalWrappedClause = BuilderUtils.castMap(rawClause);
            optionalWrappedClause.ifPresent(wrappedClause -> parsedClauses.add(new DisjunctionMaxQueryBuilder(wrappedClause)));
        }

        this.setClauses(Collections.unmodifiableList(parsedClauses));

        return this;
    }

    public static QueryBuilder builder() {
        return new QueryBuilder(new ArrayList<>());
    }

    public static QueryBuilder fromQuery(final Query query) {
        return query().setAttributesFromObject(query);
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
}
