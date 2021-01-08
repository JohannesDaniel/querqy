package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.ComparableCharSequence;
import querqy.model.BooleanQuery;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.builder.TypeCastingUtils;
import querqy.model.builder.DisjunctionMaxClauseBuilder;
import querqy.model.builder.QueryNodeBuilder;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.Occur;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static querqy.model.builder.model.BuilderFieldSettings.CLAUSES;
import static querqy.model.builder.model.BuilderFieldSettings.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldSettings.OCCUR;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DisjunctionMaxQueryBuilder implements
        QueryNodeBuilder<DisjunctionMaxQueryBuilder, DisjunctionMaxQuery, BooleanQuery> {

    public static final String NAME_OF_QUERY_TYPE = "disjunction_max_query";

    @BuilderField(settings = CLAUSES)
    private List<DisjunctionMaxClauseBuilder> clauses;

    @BuilderField(settings = OCCUR)
    private Occur occur;

    @BuilderField(settings = IS_GENERATED)
    private Boolean isGenerated;

    public DisjunctionMaxQueryBuilder(final DisjunctionMaxQuery dmq) {
        this.setAttributesFromObject(dmq);
    }

    public DisjunctionMaxQueryBuilder(final Map map) {
        this.fromMap(map);
    }

    public DisjunctionMaxQueryBuilder(final List<DisjunctionMaxClauseBuilder> clauses) {
        this.clauses = clauses;
    }

    @Override
    public DisjunctionMaxQueryBuilder getBuilder() {
        return this;
    }

    @Override
    public Class<DisjunctionMaxQueryBuilder> getBuilderClass() {
        return DisjunctionMaxQueryBuilder.class;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public DisjunctionMaxQuery buildObject(BooleanQuery parent) {
        final DisjunctionMaxQuery dmq = new DisjunctionMaxQuery(parent, this.occur.objectForClause, this.isGenerated);
        clauses.stream().map(clause -> clause.buildDisjunctionMaxClause(dmq)).forEach(dmq::addClause);

        return dmq;
    }

    @Override
    public DisjunctionMaxQueryBuilder setAttributesFromObject(final DisjunctionMaxQuery dmq) {
        final List<DisjunctionMaxClauseBuilder> clausesFromObject = dmq.getClauses().stream()
                .map(BuilderFactory::createDisjunctionMaxClauseBuilderFromObject)
                .collect(Collectors.toList());

        this.setClauses(clausesFromObject);
        this.setOccur(getOccurByClauseObject(dmq.getOccur()));
        this.setIsGenerated(dmq.isGenerated());

        return this;
    }

    @Override
    public DisjunctionMaxQueryBuilder setAttributesFromMap(final Map map) {
        this.setClauses(TypeCastingUtils.castAndParseListOfMaps(map.get(CLAUSES.fieldName),
                BuilderFactory::createDisjunctionMaxClauseBuilderFromMap));
        TypeCastingUtils.castOccurByTypeName(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        TypeCastingUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setIsGenerated);

        return this;
    }

    public static DisjunctionMaxQueryBuilder dmq(
            final List<DisjunctionMaxClauseBuilder> clauses, final Occur occur, boolean isGenerated) {
        return new DisjunctionMaxQueryBuilder(clauses, occur, isGenerated).checkAndSetDefaults();
    }

    public static DisjunctionMaxQueryBuilder dmq(final List<DisjunctionMaxClauseBuilder> clauses) {
        return dmq(clauses, null, false);
    }

    public static DisjunctionMaxQueryBuilder dmq(final DisjunctionMaxClauseBuilder... clauses) {
        return dmq(Arrays.asList(clauses));
    }

    public static DisjunctionMaxQueryBuilder dmq(final ComparableCharSequence... terms) {
        return dmq(Arrays.stream(terms).map(TermBuilder::term).collect(Collectors.toList()));
    }

    public static DisjunctionMaxQueryBuilder dmq(final String... terms) {
        return dmq(Arrays.stream(terms).map(TermBuilder::term).collect(Collectors.toList()));
    }
}
