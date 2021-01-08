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
import querqy.model.builder.TypeCastingUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.Occur;

import java.util.Map;

import static querqy.model.builder.model.BuilderFieldSettings.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldSettings.OCCUR;
import static querqy.model.builder.model.BuilderFieldSettings.RAW_QUERY;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class StringRawQueryBuilder implements QuerqyQueryBuilder<StringRawQueryBuilder, StringRawQuery, DisjunctionMaxQuery> {

    public static final String NAME_OF_QUERY_TYPE = "string_raw_query";

    @BuilderField(settings = RAW_QUERY)
    private String rawQuery;

    @BuilderField(settings = OCCUR)
    private Occur occur;

    @BuilderField(settings = IS_GENERATED)
    private Boolean isGenerated;

    public StringRawQueryBuilder(final StringRawQuery stringRawQuery) {
        this.setAttributesFromObject(stringRawQuery);
    }

    public StringRawQueryBuilder(final Map map) {
        this.fromMap(map);
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
        this.setIsGenerated(stringRawQuery.isGenerated());

        return this;
    }

    @Override
    public StringRawQueryBuilder setAttributesFromMap(Map map) {
        TypeCastingUtils.castString(map.get(RAW_QUERY.fieldName)).ifPresent(this::setRawQuery);
        TypeCastingUtils.castOccurByTypeName(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        TypeCastingUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setIsGenerated);

        return this;
    }

    public static StringRawQueryBuilder raw(final String rawQueryString) {
        return new StringRawQueryBuilder(rawQueryString);
    }
}
