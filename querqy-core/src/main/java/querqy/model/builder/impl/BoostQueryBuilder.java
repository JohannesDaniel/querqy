package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.BoostQuery;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.QueryNodeBuilder;
import querqy.model.builder.model.BuilderField;

import java.util.Map;
import java.util.Optional;

import static querqy.model.builder.TypeCastingBuilderUtils.castFloatOrDoubleToFloat;
import static querqy.model.builder.TypeCastingBuilderUtils.castMap;
import static querqy.model.builder.model.BuilderFieldProperties.BOOST;
import static querqy.model.builder.model.BuilderFieldProperties.QUERY;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BoostQueryBuilder implements QueryNodeBuilder<BoostQueryBuilder, BoostQuery, Object> {

    public static final String NAME_OF_QUERY_TYPE = "boost_query";

    @BuilderField(fieldProperties = QUERY)
    private QuerqyQueryBuilder querqyQueryBuilder;

    @BuilderField(fieldProperties = BOOST)
    private float boost;

    public BoostQueryBuilder(final BoostQuery boostQuery) {
        this.setAttributesFromObject(boostQuery);
    }

    public BoostQueryBuilder(final Map map) {
        this.fromMap(map);
    }

    public BoostQueryBuilder(final QuerqyQueryBuilder querqyQueryBuilder) {
        this.querqyQueryBuilder = querqyQueryBuilder;
    }

    @Override
    public BoostQueryBuilder getBuilder() {
        return this;
    }

    @Override
    public Class<BoostQueryBuilder> getBuilderClass() {
        return BoostQueryBuilder.class;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public BoostQuery buildObject(Object parent) {
        return new BoostQuery(querqyQueryBuilder.buildQuerqyQuery(), this.boost);
    }

    @Override
    public BoostQueryBuilder setAttributesFromObject(final BoostQuery boostQuery) {
        this.setQuerqyQueryBuilder(BuilderFactory.createQuerqyQueryBuilderFromObject(boostQuery.getQuery()));
        this.setBoost(boostQuery.getBoost());

        return this;
    }

    @Override
    public BoostQueryBuilder setAttributesFromMap(final Map map) {
        final Optional<Map> optionalQuerqyQuery = castMap(map.get(QUERY.fieldName));

        if (optionalQuerqyQuery.isPresent()) {
            this.setQuerqyQueryBuilder(BuilderFactory.createQuerqyQueryBuilderFromMap(optionalQuerqyQuery.get()));
        } else {
            throw new QueryBuilderException("The query of a boost query must not be null");
        }

        castFloatOrDoubleToFloat(map.get(BOOST.fieldName)).ifPresent(this::setBoost);

        return this;
    }

    public static BoostQueryBuilder boost(final QuerqyQueryBuilder querqyQueryBuilder) {
        return new BoostQueryBuilder(querqyQueryBuilder);
    }

    public static BoostQueryBuilder boost(final QuerqyQueryBuilder querqyQueryBuilder, final float boost) {
        return new BoostQueryBuilder(querqyQueryBuilder, boost);
    }
}
