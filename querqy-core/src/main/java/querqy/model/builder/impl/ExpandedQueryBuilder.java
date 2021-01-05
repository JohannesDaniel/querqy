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
import querqy.model.builder.model.BuilderField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static querqy.model.builder.model.BuilderFieldProperties.BOOST_DOWN_QUERIES;
import static querqy.model.builder.model.BuilderFieldProperties.BOOST_UP_QUERIES;
import static querqy.model.builder.model.BuilderFieldProperties.FILTER_QUERIES;
import static querqy.model.builder.model.BuilderFieldProperties.USER_QUERY;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ExpandedQueryBuilder implements QueryNodeBuilder<ExpandedQueryBuilder, ExpandedQuery, Object> {

    public static final String NAME_OF_QUERY_TYPE = "expanded_query";

    @BuilderField(fieldProperties = USER_QUERY)
    private QuerqyQueryBuilder userQuery;

    @BuilderField(fieldProperties = FILTER_QUERIES)
    private List<QuerqyQueryBuilder> filterQueries;

    @BuilderField(fieldProperties = BOOST_UP_QUERIES)
    private List<BoostQueryBuilder> boostUpQueries;

    @BuilderField(fieldProperties = BOOST_DOWN_QUERIES)
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
    public ExpandedQueryBuilder getBuilder() {
        return this;
    }

    @Override
    public Class<ExpandedQueryBuilder> getBuilderClass() {
        return ExpandedQueryBuilder.class;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public ExpandedQuery buildObject(Object parent) {
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
    public Map<String, Object> attributesToMap() {
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

        this.setFilterQueries(BuilderUtils.castAndParseListOfMaps(map.get(FILTER_QUERIES.fieldName),
                BuilderFactory::createQuerqyQueryBuilderFromMap));

        this.setBoostUpQueries(BuilderUtils.castAndParseListOfMaps(map.get(BOOST_UP_QUERIES.fieldName), BoostQueryBuilder::new));
        this.setBoostDownQueries(BuilderUtils.castAndParseListOfMaps(map.get(BOOST_DOWN_QUERIES.fieldName), BoostQueryBuilder::new));

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
