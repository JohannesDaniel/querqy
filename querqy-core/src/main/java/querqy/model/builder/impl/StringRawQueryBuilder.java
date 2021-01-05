package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.QuerqyQuery;
import querqy.model.StringRawQuery;
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.model.Occur;

import java.util.Map;

import static java.util.Objects.isNull;
import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldProperties.OCCUR;
import static querqy.model.builder.model.BuilderFieldProperties.QUERY;
import static querqy.model.builder.model.Occur.SHOULD;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class StringRawQueryBuilder implements QuerqyQueryBuilder<StringRawQueryBuilder, StringRawQuery, DisjunctionMaxQuery> {

    public static final String NAME_OF_QUERY_TYPE = "string_raw_query";

    private Occur occur;
    private boolean isGenerated;
    private String rawQuery;

    public StringRawQueryBuilder(final StringRawQuery stringRawQuery) {
        this.setAttributesFromObject(stringRawQuery);
    }

    public StringRawQueryBuilder(final Map map) {
        this.setAttributesFromWrappedMap(map);
    }

    public StringRawQueryBuilder(final String rawQuery) {
        this.setRawQuery(rawQuery);
    }

    @Override
    public void setDefaults() {
        if (isNull(this.occur)) {
            this.setOccur(SHOULD);
        }
    }

    @Override
    public QuerqyQuery<?> buildQuerqyQuery() {
        return build(null);
    }

    @Override
    public StringRawQuery build(final DisjunctionMaxQuery parent) {
        setDefaults();

        if (isNull(this.rawQuery)) {
            throw new QueryBuilderException(String.format("The query string of %s must not be null", NAME_OF_QUERY_TYPE));
        }

        return new StringRawQuery(parent, this.rawQuery, this.occur.objectForClause, this.isGenerated);
    }

    @Override
    public StringRawQueryBuilder setAttributesFromObject(StringRawQuery stringRawQuery) {
        this.setRawQuery(stringRawQuery.getQueryString());
        this.setOccur(getOccurByClauseObject(stringRawQuery.getOccur()));
        this.setGenerated(stringRawQuery.isGenerated());

        return this;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public Map<String, Object> attributesToMap() {
        setDefaults();

        if (isNull(this.rawQuery)) {
            throw new QueryBuilderException(String.format("The query string of %s must not be null", NAME_OF_QUERY_TYPE));
        }

        final QueryBuilderMap map = new QueryBuilderMap();
        map.put(QUERY.fieldName, this.rawQuery);
        map.put(OCCUR.fieldName, this.occur.typeName);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public StringRawQueryBuilder setAttributesFromMap(Map map) {
        BuilderUtils.castString(map.get(QUERY.fieldName)).ifPresent(this::setRawQuery);
        BuilderUtils.castOccur(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        BuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setGenerated);

        return this;
    }

    public static StringRawQueryBuilder raw(final String rawQueryString) {
        return new StringRawQueryBuilder(rawQueryString);
    }
}
