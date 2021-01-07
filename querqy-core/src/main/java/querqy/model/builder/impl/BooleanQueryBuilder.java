package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.BooleanQuery;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.QuerqyQuery;
import querqy.model.Query;
import querqy.model.builder.TypeCastingBuilderUtils;
import querqy.model.builder.DisjunctionMaxClauseBuilder;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.Occur;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static querqy.model.builder.model.BuilderFieldProperties.CLAUSES;
import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldProperties.OCCUR;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class BooleanQueryBuilder implements DisjunctionMaxClauseBuilder<BooleanQueryBuilder, BooleanQuery>,
        QuerqyQueryBuilder<BooleanQueryBuilder, BooleanQuery, DisjunctionMaxQuery> {

    public static final String NAME_OF_QUERY_TYPE = "boolean_query";

    @BuilderField(fieldProperties = CLAUSES)
    private List<DisjunctionMaxQueryBuilder> clauses;

    @BuilderField(fieldProperties = OCCUR)
    private Occur occur;

    @BuilderField(fieldProperties = IS_GENERATED)
    private boolean isGenerated;

    public BooleanQueryBuilder(final BooleanQuery bq) {
        this.setAttributesFromObject(bq);
    }

    public BooleanQueryBuilder(final Map map) {
        this.fromMap(map);
    }

    public BooleanQueryBuilder(final List<DisjunctionMaxQueryBuilder> clauses) {
        this.clauses = clauses;
    }

    @Override
    public BooleanQueryBuilder getBuilder() {
        return this;
    }

    @Override
    public Class<BooleanQueryBuilder> getBuilderClass() {
        return BooleanQueryBuilder.class;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public BooleanQuery buildObject(final DisjunctionMaxQuery parent) {
        final BooleanQuery bq = new BooleanQuery(parent, this.occur.objectForClause, this.isGenerated);
        clauses.stream().map(dmq -> dmq.build(bq)).forEach(bq::addClause);
        return bq;
    }

    @Override
    public QuerqyQuery<?> buildQuerqyQuery() {
        checkAndSetDefaults();

        final Query query = new Query(isGenerated);
        clauses.stream().map(clause -> clause.build(query)).forEach(query::addClause);
        return query;
    }

    @Override
    public BooleanQueryBuilder setAttributesFromObject(final BooleanQuery bq) {

        final List<DisjunctionMaxQueryBuilder> clausesFromObject = bq.getClauses().stream()
                .map(clause -> {

                    if (clause instanceof DisjunctionMaxQuery) {
                        return new DisjunctionMaxQueryBuilder((DisjunctionMaxQuery) clause);

                    } else {
                        throw new QueryBuilderException("The structure of this query is currently not supported by builders");
                    }})

                .collect(Collectors.toList());

        this.setClauses(clausesFromObject);
        this.setOccur(getOccurByClauseObject(bq.getOccur()));
        this.setGenerated(bq.isGenerated());

        return this;
    }

    @Override
    public BooleanQueryBuilder setAttributesFromMap(final Map map) {
        this.setClauses(TypeCastingBuilderUtils.castAndParseListOfMaps(map.get(CLAUSES.fieldName), DisjunctionMaxQueryBuilder::new));

        TypeCastingBuilderUtils.castOccurByTypeName(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        TypeCastingBuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setGenerated);

        return this;
    }

    public static BooleanQueryBuilder bq(final List<DisjunctionMaxQueryBuilder> dmqs, final Occur occur, boolean isGenerated) {
        final BooleanQueryBuilder bq = new BooleanQueryBuilder(dmqs, occur, isGenerated);
        bq.checkAndSetDefaults();

        return bq;
    }

    public static BooleanQueryBuilder bq(final List<DisjunctionMaxQueryBuilder> dmqs) {
        return bq(dmqs, null, false);
    }

    public static BooleanQueryBuilder bq(final DisjunctionMaxQueryBuilder... dmqs) {
        return bq(Arrays.stream(dmqs).collect(Collectors.toList()));
    }

    public static BooleanQueryBuilder bq(final String... dmqs) {
        return bq(Arrays.stream(dmqs).map(DisjunctionMaxQueryBuilder::dmq).collect(Collectors.toList()));
    }
}
