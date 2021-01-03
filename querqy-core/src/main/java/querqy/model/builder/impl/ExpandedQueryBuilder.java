package querqy.model.builder.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.ExpandedQuery;
import querqy.model.QuerqyQuery;
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.TopLevelNodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static querqy.model.builder.model.MapField.FILTER_QUERIES;
import static querqy.model.builder.model.MapField.USER_QUERY;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class ExpandedQueryBuilder implements TopLevelNodeBuilder<ExpandedQueryBuilder, ExpandedQuery> {

    private static final String NAME_OF_QUERY_TYPE = "expanded_query";

    // TODO: ensure that this is never null
    private QuerqyQueryBuilder userQuery;
    private List<QuerqyQueryBuilder> filterQueries;

    @Override
    public void setDefaults() {

    }

    @Override
    public ExpandedQuery build() {
        final ExpandedQuery expandedQuery = new ExpandedQuery(userQuery.buildQuerqyQuery());

        for (final QuerqyQueryBuilder filterQuery : filterQueries) {
            expandedQuery.addFilterQuery(filterQuery.buildQuerqyQuery());
        }

        return expandedQuery;
    }

    @Override
    public ExpandedQueryBuilder setAttributesFromObject(final ExpandedQuery expanded) {
        this.setUserQuery(BuilderFactory.createQuerqyQueryBuilderFromObject(expanded.getUserQuery()));

        // TODO: Change expanded query to return empty list instead of null
        final Collection<QuerqyQuery<?>> filterQueries = expanded.getFilterQueries();
        if (nonNull(filterQueries)) {
            this.setFilterQueries(filterQueries.stream()
                    .map(BuilderFactory::createQuerqyQueryBuilderFromObject)
                    .collect(Collectors.toList()));
        }

        return this;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public Map attributesToMap() {
        final BuilderUtils.QueryBuilderMap map = new BuilderUtils.QueryBuilderMap();

        map.put(USER_QUERY.fieldName, this.userQuery.toMap());
        map.put(FILTER_QUERIES.fieldName, filterQueries.stream().map(QuerqyQueryBuilder::toMap).collect(Collectors.toList()));

        return map;
    }

    @Override
    public ExpandedQueryBuilder setAttributesFromMap(final Map map) {

        final Optional<Map> optionalUserQuery = BuilderUtils.castMap(map.get(USER_QUERY.fieldName));
        if (optionalUserQuery.isPresent()) {
            setUserQuery(BuilderFactory.createQuerqyQueryBuilderFromMap(optionalUserQuery.get()));

        } else {
            throw new QueryBuilderException(String.format("Creating %s requires an entry %s", NAME_OF_QUERY_TYPE, USER_QUERY.fieldName));
        }

        final List<QuerqyQueryBuilder> parsedFilterQueries = new ArrayList<>();

        final Optional<List> optionalFilterQueries = BuilderUtils.castList(map.get(FILTER_QUERIES.fieldName));
        if (optionalFilterQueries.isPresent()) {
            for (final Object rawFilterQuery : optionalFilterQueries.get()) {
                BuilderUtils.castMap(rawFilterQuery).ifPresent(filterQuery ->
                        parsedFilterQueries.add(BuilderFactory.createQuerqyQueryBuilderFromMap(filterQuery)));
            }
        }

        setFilterQueries(Collections.unmodifiableList(parsedFilterQueries));

        return this;
    }

    // TODO: change this -> userquery not null
    public static ExpandedQueryBuilder expanded() {
        return new ExpandedQueryBuilder();
    }

    public static ExpandedQueryBuilder expanded(final Map map) {
        return new ExpandedQueryBuilder().setAttributesFromWrappedMap(map);
    }

    public static ExpandedQueryBuilder expanded(final QuerqyQueryBuilder userQuery) {
        return new ExpandedQueryBuilder(userQuery, Collections.emptyList());
    }

    public static ExpandedQueryBuilder expanded(
            final QueryBuilder userQuery,
            final List<?> boostUpQueries,
            final List<?> boostDownQueries,
            final List<?> filterQueries) {
        return null;

    }


}
