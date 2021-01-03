package querqy.model.builder.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.ComparableCharSequence;
import querqy.model.BooleanQuery;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.DisjunctionMaxClauseBuilder;
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
public class DisjunctionMaxQueryBuilder implements
        QueryNodeBuilder<DisjunctionMaxQueryBuilder, DisjunctionMaxQuery, BooleanQuery> {

    public static final String NAME_OF_QUERY_TYPE = "disjunction_max_query";

    private List<DisjunctionMaxClauseBuilder> clauses;
    private Occur occur;
    private boolean isGenerated;

    public DisjunctionMaxQueryBuilder(final DisjunctionMaxQuery dmq) {
        this.setAttributesFromObject(dmq);
        this.setDefaults();
    }

    public DisjunctionMaxQueryBuilder(final Map map) {
        this.setAttributesFromWrappedMap(map);
        this.setDefaults();
    }

    public DisjunctionMaxQueryBuilder(final List<DisjunctionMaxClauseBuilder> clauses) {
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
    public DisjunctionMaxQuery build(final BooleanQuery parent) {
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
        this.setGenerated(dmq.isGenerated());

        return this;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public Map<String, Object> attributesToMap() {
        final BuilderUtils.QueryBuilderMap map = new BuilderUtils.QueryBuilderMap();

        map.put(CLAUSES.fieldName, clauses.stream().map(QueryNodeBuilder::toMap).collect(Collectors.toList()));
        map.put(OCCUR.fieldName, this.occur.typeName);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public DisjunctionMaxQueryBuilder setAttributesFromMap(final Map map) {
        final List<DisjunctionMaxClauseBuilder> parsedClauses = new ArrayList<>();

        final List rawClauses = BuilderUtils.castList(map.get(CLAUSES.fieldName)).orElse(Collections.emptyList());

        for (final Object rawClause : rawClauses) {
            final Optional<Map> optionalClause = BuilderUtils.castMap(rawClause);
            optionalClause.ifPresent(wrappedClause -> parsedClauses.add(
                    BuilderFactory.createDisjunctionMaxClauseBuilderFromMap(wrappedClause)));
        }

        this.setClauses(Collections.unmodifiableList(parsedClauses));
        BuilderUtils.castOccur(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        BuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setGenerated);

        return this;
    }

    public static DisjunctionMaxQueryBuilder dmq(
            final List<DisjunctionMaxClauseBuilder> clauses, final Occur occur, boolean isGenerated) {
        return new DisjunctionMaxQueryBuilder(clauses, occur, isGenerated);
    }

    public static DisjunctionMaxQueryBuilder dmq(final List<DisjunctionMaxClauseBuilder> clauses) {
        return new DisjunctionMaxQueryBuilder(clauses);
    }

    public static DisjunctionMaxQueryBuilder dmq(final DisjunctionMaxClauseBuilder... clauses) {
        return new DisjunctionMaxQueryBuilder(Arrays.asList(clauses));
    }

    public static DisjunctionMaxQueryBuilder dmq(final ComparableCharSequence... terms) {
        return new DisjunctionMaxQueryBuilder(
                Arrays.stream(terms).map(TermBuilder::term).collect(Collectors.toList()));
    }

    public static DisjunctionMaxQueryBuilder dmq(final String... terms) {
        return new DisjunctionMaxQueryBuilder(
                Arrays.stream(terms).map(TermBuilder::term).collect(Collectors.toList()));
    }
}
