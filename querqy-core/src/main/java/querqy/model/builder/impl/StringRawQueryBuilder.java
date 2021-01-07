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
import querqy.model.builder.TypeCastingBuilderUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.Occur;
import querqy.model.builder.model.QueryBuilderMap;

import java.util.Map;

import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldProperties.OCCUR;
import static querqy.model.builder.model.BuilderFieldProperties.QUERY;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class StringRawQueryBuilder implements QuerqyQueryBuilder<StringRawQueryBuilder, StringRawQuery, DisjunctionMaxQuery> {

    public static final String NAME_OF_QUERY_TYPE = "string_raw_query";

    @BuilderField(fieldProperties = OCCUR)
    private Occur occur;

    @BuilderField(fieldProperties = IS_GENERATED)
    private boolean isGenerated;

    @BuilderField(fieldProperties = QUERY)
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
    public StringRawQueryBuilder getBuilder() {
        return this;
    }

    @Override
    public Class<StringRawQueryBuilder> getBuilderClass() {
        return StringRawQueryBuilder.class;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public QuerqyQuery<?> buildQuerqyQuery() {
        return build(null);
    }

    @Override
    public StringRawQuery buildObject(DisjunctionMaxQuery parent) {
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
    public Map<String, Object> attributesToMap() {
        final QueryBuilderMap map = new QueryBuilderMap();
        map.put(QUERY.fieldName, this.rawQuery);
        map.put(OCCUR.fieldName, this.occur.typeName);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public StringRawQueryBuilder setAttributesFromMap(Map map) {
        TypeCastingBuilderUtils.castString(map.get(QUERY.fieldName)).ifPresent(this::setRawQuery);
        TypeCastingBuilderUtils.castOccurByTypeName(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        TypeCastingBuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setGenerated);

        return this;
    }

    public static StringRawQueryBuilder raw(final String rawQueryString) {
        return new StringRawQueryBuilder(rawQueryString);
    }
}
