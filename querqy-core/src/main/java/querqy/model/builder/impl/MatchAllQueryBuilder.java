package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.MatchAllQuery;
import querqy.model.builder.TypeCastingUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.Occur;

import java.util.Map;

import static querqy.model.builder.model.BuilderFieldSettings.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldSettings.OCCUR;
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

    @BuilderField(settings = OCCUR)
    private Occur occur;

    @BuilderField(settings = IS_GENERATED)
    private Boolean isGenerated;

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
        return new MatchAllQuery(parent, this.occur.objectForClause, isGenerated);
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
        TypeCastingUtils.castOccurByTypeName(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        TypeCastingUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setIsGenerated);
        return this;
    }

    public static MatchAllQueryBuilder matchall(final Occur occur, final boolean isGenerated) {
        return new MatchAllQueryBuilder(occur, isGenerated);
    }

    public static MatchAllQueryBuilder matchall() {
        return new MatchAllQueryBuilder();
    }

}
