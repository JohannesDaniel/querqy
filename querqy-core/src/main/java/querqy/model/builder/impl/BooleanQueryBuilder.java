package querqy.model.builder.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.BooleanQuery;
import querqy.model.DisjunctionMaxClause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.QuerqyQuery;
import querqy.model.Query;
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.DisjunctionMaxClauseBuilder;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.QueryNodeBuilder;
import querqy.model.builder.model.Occur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static querqy.model.builder.impl.DisjunctionMaxQueryBuilder.dmq;
import static querqy.model.builder.model.MapField.CLAUSES;
import static querqy.model.builder.model.MapField.IS_GENERATED;
import static querqy.model.builder.model.MapField.OCCUR;
import static querqy.model.builder.model.Occur.SHOULD;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BooleanQueryBuilder implements DisjunctionMaxClauseBuilder<BooleanQueryBuilder, BooleanQuery>,
        QuerqyQueryBuilder<BooleanQueryBuilder, BooleanQuery, DisjunctionMaxQuery> {

    public static final String NAME_OF_QUERY_TYPE = "boolean_query";

    private List<DisjunctionMaxQueryBuilder> clauses;
    private Occur occur;
    private boolean isGenerated;

    public BooleanQueryBuilder(final BooleanQuery bq) {
        this.setAttributesFromObject(bq);
        this.setDefaults();
    }

    public BooleanQueryBuilder(final Map map) {
        this.setAttributesFromWrappedMap(map);
        this.setDefaults();
    }

    public BooleanQueryBuilder(final List<DisjunctionMaxQueryBuilder> clauses) {
        this.clauses = clauses;
        this.setDefaults();
    }

    @Override
    public void setDefaults() {
        if (isNull(this.clauses)) {
            this.setClauses(Collections.emptyList());
        }

        if (isNull(this.occur)) {
            this.setOccur(SHOULD);
        }
    }

    @Override
    public BooleanQuery build(final DisjunctionMaxQuery parent) {
        final BooleanQuery bq = new BooleanQuery(parent, this.occur.objectForClause, this.isGenerated);
        clauses.stream().map(dmq -> dmq.build(bq)).forEach(bq::addClause);
        return bq;
    }

    @Override
    public DisjunctionMaxClause buildDisjunctionMaxClause(final DisjunctionMaxQuery parent) {
        return this.build(parent);
    }

    @Override
    public QuerqyQuery<?> buildQuerqyQuery() {
        final Query query = new Query();
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
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public Map attributesToMap() {
        final BuilderUtils.QueryBuilderMap map = new BuilderUtils.QueryBuilderMap();

        map.put(CLAUSES.fieldName, clauses.stream().map(QueryNodeBuilder::toMap).collect(Collectors.toList()));
        map.put(OCCUR.fieldName, this.occur.typeName);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public BooleanQueryBuilder setAttributesFromMap(final Map map) {
        final List<DisjunctionMaxQueryBuilder> parsedClauses = new ArrayList<>();

        final List rawClauses = BuilderUtils.castList(map.get(CLAUSES.fieldName)).orElse(Collections.emptyList());

        for (final Object rawClause : rawClauses) {
            final Optional<Map> optionalWrappedClause = BuilderUtils.castMap(rawClause);

            optionalWrappedClause.ifPresent(
                    wrappedClause -> parsedClauses.add(new DisjunctionMaxQueryBuilder(wrappedClause)));
        }

        this.setClauses(Collections.unmodifiableList(parsedClauses));
        BuilderUtils.castOccur(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        BuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setGenerated);

        return this;
    }

    public static BooleanQueryBuilder bq(final Map map) {
        return new BooleanQueryBuilder(map);
    }

    public static BooleanQueryBuilder bq(final List<DisjunctionMaxQueryBuilder> dmqs) {
        return new BooleanQueryBuilder(dmqs);
    }

    public static BooleanQueryBuilder bq(final DisjunctionMaxQueryBuilder... dmqs) {
        return new BooleanQueryBuilder(Arrays.stream(dmqs).collect(Collectors.toList()));
    }

    public static BooleanQueryBuilder bq(final String... dmqs) {
        return new BooleanQueryBuilder(Arrays.stream(dmqs).map(DisjunctionMaxQueryBuilder::dmq).collect(Collectors.toList()));
    }
}
