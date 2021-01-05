package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.BoostQuery;
import querqy.model.ExpandedQuery;
import querqy.model.QuerqyQuery;
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.QueryNodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static querqy.model.builder.model.MapField.BOOST_DOWN_QUERIES;
import static querqy.model.builder.model.MapField.BOOST_UP_QUERIES;
import static querqy.model.builder.model.MapField.FILTER_QUERIES;
import static querqy.model.builder.model.MapField.USER_QUERY;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ExpandedQueryBuilder implements QueryNodeBuilder<ExpandedQueryBuilder, ExpandedQuery, Object> {

    public static final String NAME_OF_QUERY_TYPE = "expanded_query";

    private QuerqyQueryBuilder userQuery;
    private List<QuerqyQueryBuilder> filterQueries;
    private List<BoostQueryBuilder> boostUpQueries;
    private List<BoostQueryBuilder> boostDownQueries;

    public ExpandedQueryBuilder(final ExpandedQuery expanded) {
        this.setAttributesFromObject(expanded);
    }

    public ExpandedQueryBuilder(final Map map) {
        this.setAttributesFromWrappedMap(map);
    }

    public ExpandedQueryBuilder(final QuerqyQueryBuilder userQuery) {
        this.userQuery = userQuery;
    }


    @Override
    public void setDefaults() {
        if (isNull(this.filterQueries)) {
            this.setFilterQueries(Collections.emptyList());
        }

        if (isNull(this.boostUpQueries)) {
            this.setBoostUpQueries(Collections.emptyList());
        }

        if (isNull(this.boostDownQueries)) {
            this.setBoostDownQueries(Collections.emptyList());
        }
    }

    @Override
    public ExpandedQuery build() {
        setDefaults();

        if (isNull(this.userQuery)) {
            throw new QueryBuilderException("UserQuery must not be null");
        }

        final ExpandedQuery expandedQuery = new ExpandedQuery(userQuery.buildQuerqyQuery());

        for (final QuerqyQueryBuilder filterQuery : filterQueries) {
            expandedQuery.addFilterQuery(filterQuery.buildQuerqyQuery());
        }

        for (final BoostQueryBuilder boostUpQuery : boostUpQueries) {
            expandedQuery.addBoostUpQuery(boostUpQuery.build());
        }

        for (final BoostQueryBuilder boostDownQuery : boostDownQueries) {
            expandedQuery.addBoostDownQuery(boostDownQuery.build());
        }

        return expandedQuery;
    }

    @Override
    public ExpandedQuery build(Object parent) {
        throw new QueryBuilderException("Not allowed to set parent node for this Builder");
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

        // TODO: Change expanded query to return empty list instead of null
        final Collection<BoostQuery> boostUpQueries = expanded.getBoostUpQueries();
        if (nonNull(boostUpQueries)) {
            this.setBoostUpQueries(boostUpQueries.stream().map(BoostQueryBuilder::new).collect(Collectors.toList()));
        }

        // TODO: Change expanded query to return empty list instead of null
        final Collection<BoostQuery> boostDownQueries = expanded.getBoostDownQueries();
        if (nonNull(boostDownQueries)) {
            this.setBoostDownQueries(boostDownQueries.stream().map(BoostQueryBuilder::new).collect(Collectors.toList()));
        }

        return this;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public Map<String, Object> attributesToMap() {
        setDefaults();

        if (isNull(this.userQuery)) {
            throw new QueryBuilderException("UserQuery must not be null");
        }

        final QueryBuilderMap map = new QueryBuilderMap();

        map.put(USER_QUERY.fieldName, this.userQuery.toMap());
        map.put(FILTER_QUERIES.fieldName, filterQueries.stream().map(QuerqyQueryBuilder::toMap).collect(Collectors.toList()));
        map.put(BOOST_UP_QUERIES.fieldName, boostUpQueries.stream().map(BoostQueryBuilder::toMap).collect(Collectors.toList()));
        map.put(BOOST_DOWN_QUERIES.fieldName, boostDownQueries.stream().map(BoostQueryBuilder::toMap).collect(Collectors.toList()));

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

        final List<BoostQueryBuilder> parsedBoostUpQueries = new ArrayList<>();
        final Optional<List> optionalBoostUpQueries = BuilderUtils.castList(map.get(BOOST_UP_QUERIES.fieldName));
        if (optionalBoostUpQueries.isPresent()) {
            for (final Object rawBoostUpQuery : optionalBoostUpQueries.get()) {
                BuilderUtils.castMap(rawBoostUpQuery).ifPresent(boostUpQuery ->
                        parsedBoostUpQueries.add(new BoostQueryBuilder(boostUpQuery)));
            }
        }
        setBoostUpQueries(Collections.unmodifiableList(parsedBoostUpQueries));

        final List<BoostQueryBuilder> parsedBoostDownQueries = new ArrayList<>();
        final Optional<List> optionalBoostDownQueries = BuilderUtils.castList(map.get(BOOST_DOWN_QUERIES.fieldName));
        if (optionalBoostDownQueries.isPresent()) {
            for (final Object rawBoostDownQuery : optionalBoostDownQueries.get()) {
                BuilderUtils.castMap(rawBoostDownQuery).ifPresent(boostDownQuery ->
                        parsedBoostDownQueries.add(new BoostQueryBuilder(boostDownQuery)));
            }
        }
        setBoostDownQueries(Collections.unmodifiableList(parsedBoostDownQueries));

        return this;
    }

    public static ExpandedQueryBuilder expanded(final Map map) {
        return new ExpandedQueryBuilder(map);
    }

    public static ExpandedQueryBuilder expanded(final ExpandedQuery expanded) {
        return new ExpandedQueryBuilder(expanded);
    }

    public static ExpandedQueryBuilder expanded(final QuerqyQueryBuilder userQuery, final QuerqyQueryBuilder... filters) {
        return new ExpandedQueryBuilder(userQuery, Arrays.asList(filters), Collections.emptyList(), Collections.emptyList());
    }

    public static ExpandedQueryBuilder expanded(final QuerqyQueryBuilder userQuery, final BoostQueryBuilder... boostUps) {
        return new ExpandedQueryBuilder(userQuery, Collections.emptyList(), Arrays.asList(boostUps), Collections.emptyList());
    }

    public static ExpandedQueryBuilder expanded(final QuerqyQueryBuilder userQuery,
                                                final List<QuerqyQueryBuilder> filters,
                                                final List<BoostQueryBuilder> boostUps,
                                                final List<BoostQueryBuilder> boostDowns) {
        return new ExpandedQueryBuilder(userQuery, filters, boostUps, boostDowns);
    }

    public static ExpandedQueryBuilder expanded(final QuerqyQueryBuilder userQuery) {
        return new ExpandedQueryBuilder(userQuery);
    }

}
