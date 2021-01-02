package querqy.model.builder;

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
import querqy.model.Term;
import querqy.model.builder.model.Occur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static querqy.model.builder.model.MapField.CLAUSES;
import static querqy.model.builder.model.MapField.IS_GENERATED;
import static querqy.model.builder.model.MapField.OCCUR;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class DisjunctionMaxQueryBuilder implements QuerqyQueryBuilder<DisjunctionMaxQueryBuilder, DisjunctionMaxQuery, BooleanQuery> {

    private static final String BUILDER_NAME = "dmq";

    private List<DisjunctionMaxClauseBuilder> clauses;
    private Occur occur;
    private boolean isGenerated;

    @Override
    public DisjunctionMaxQuery build(final BooleanQuery parent) {
        final DisjunctionMaxQuery dmq = new DisjunctionMaxQuery(parent, this.occur.objectForClause, this.isGenerated);
        getClauses().stream().map(clause -> clause.buildDmc(dmq)).forEach(dmq::addClause);

        return dmq;
    }

    @Override
    public DisjunctionMaxQueryBuilder setAttributesFromObject(final DisjunctionMaxQuery dmq) {
        final List<DisjunctionMaxClauseBuilder> clausesFromObject = dmq.getClauses().stream()
                .map(clause -> {

                    if (clause instanceof Term) {
                        return TermBuilder.fromQuery((Term) clause);

                    } else if (clause instanceof BooleanQuery){
                        return BooleanQueryBuilder.fromQuery((BooleanQuery) clause);

                    } else {
                        throw new QueryBuilderException("The structure of this query is currently not supported by builders");
                    }})

                .collect(Collectors.toList());

        this.setClauses(clausesFromObject);
        this.setOccur(getOccurByClauseObject(dmq.getOccur()));
        this.setGenerated(dmq.isGenerated());

        return this;
    }

    @Override
    public String getBuilderName() {
        return BUILDER_NAME;
    }

    @Override
    public Map<String, Object> attributesToMap() {
        final BuilderUtils.QueryBuilderMap map = new BuilderUtils.QueryBuilderMap();

        map.put(CLAUSES.fieldName, clauses.stream().map(QuerqyQueryBuilder::toMap).collect(Collectors.toList()));
        map.put(OCCUR.fieldName, this.occur.typeName);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public DisjunctionMaxQueryBuilder setAttributesFromMap(final Map map) {
        final List<DisjunctionMaxClauseBuilder> parsedClauses = new ArrayList<>();

        // TODO: Map should also be fine here as SingletonList
        final List rawClauses = BuilderUtils.castList(map.get(CLAUSES.fieldName)).orElse(Collections.emptyList());

        for (final Object rawClause : rawClauses) {
            final Optional<Map> optionalClause = BuilderUtils.castMap(rawClause);

            if (optionalClause.isPresent()) {
                final Map wrappedClause = optionalClause.get();
                final String keyOfClause = BuilderUtils.expectMapToContainExactlyOneEntryAndGetKey(wrappedClause);

                if (TermBuilder.TYPE_NAME.equals(keyOfClause)) {
                    parsedClauses.add(TermBuilder.term().setAttributesFromWrappedMap(wrappedClause));

                } else if (false) {
                    // TODO: BQ

                } else {
                    throw new QueryBuilderException(String.format("Clauses of name %s are unknown", keyOfClause));
                }
            }
        }

        this.setClauses(Collections.unmodifiableList(parsedClauses));
        BuilderUtils.castOccur(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        BuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setGenerated);

        return this;
    }

    public static DisjunctionMaxQueryBuilder fromQuery(final DisjunctionMaxQuery dmq) {
        return dmq().setAttributesFromObject(dmq);
    }

    public static DisjunctionMaxQueryBuilder dmq() {
        return dmq(Collections.emptyList());
    }

    public static DisjunctionMaxQueryBuilder dmq(final List<DisjunctionMaxClauseBuilder> clauses, final Occur occur, boolean isGenerated) {
        return new DisjunctionMaxQueryBuilder(clauses, occur, isGenerated);
    }

    public static DisjunctionMaxQueryBuilder dmq(final List<DisjunctionMaxClauseBuilder> clauses) {
        return dmq(clauses, Occur.SHOULD, false);
    }

    public static DisjunctionMaxQueryBuilder dmq(final DisjunctionMaxClauseBuilder... clauses) {
        return dmq(Arrays.asList(clauses));
    }

    public static DisjunctionMaxQueryBuilder dmq(final ComparableCharSequence... terms) {
        return dmq(Arrays.stream(terms).map(TermBuilder::term).toArray(DisjunctionMaxClauseBuilder[]::new));
    }

    public static DisjunctionMaxQueryBuilder dmq(final String... terms) {
        return dmq(Arrays.stream(terms).map(TermBuilder::term).toArray(DisjunctionMaxClauseBuilder[]::new));
    }
}
