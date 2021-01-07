package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.Clause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.MatchAllQuery;
import querqy.model.builder.TypeCastingBuilderUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.Occur;
import querqy.model.builder.model.QueryBuilderMap;

import java.util.Map;

import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldProperties.OCCUR;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MatchAllQueryBuilder implements QuerqyQueryBuilder<MatchAllQueryBuilder, MatchAllQuery, DisjunctionMaxQuery> {

    public static final String NAME_OF_QUERY_TYPE = "match_all_query";

    @BuilderField(fieldProperties = IS_GENERATED)
    private Boolean isGenerated;

    @BuilderField(fieldProperties = OCCUR)
    private Occur occur;

    public MatchAllQueryBuilder(final MatchAllQuery matchAllQuery) {
        this.setAttributesFromObject(matchAllQuery);
    }

    public MatchAllQueryBuilder(final Map map) {
        this.fromMap(map);
    }

    @Override
    public Class<MatchAllQueryBuilder> getBuilderClass() {
        return MatchAllQueryBuilder.class;
    }

    @Override
    public MatchAllQueryBuilder getBuilder() {
        return this;
    }

    @Override
    public MatchAllQuery buildObject(DisjunctionMaxQuery parent) {
        return new MatchAllQuery(parent, Clause.Occur.SHOULD, isGenerated);
    }

    @Override
    public MatchAllQueryBuilder setAttributesFromObject(MatchAllQuery matchAllQuery) {
        this.setOccur(getOccurByClauseObject(matchAllQuery.getOccur()));
        this.setIsGenerated(matchAllQuery.isGenerated());
        return this;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public MatchAllQueryBuilder setAttributesFromMap(Map map) {
        TypeCastingBuilderUtils.castOccurByTypeName(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        TypeCastingBuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setIsGenerated);
        return this;
    }

    public static MatchAllQueryBuilder matchall() {
        return new MatchAllQueryBuilder();
    }

}
