package querqy.model.builder;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import querqy.model.BooleanQuery;
import querqy.model.Clause;
import querqy.model.DisjunctionMaxClause;
import querqy.model.DisjunctionMaxQuery;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
public class BooleanQueryBuilder implements DisjunctionMaxClauseBuilder<BooleanQueryBuilder, BooleanQuery> {

    public static final String CLAUSE_NAME = "bq";

    private DisjunctionMaxQuery parent;

    private final List<DisjunctionMaxQueryBuilder> clauses;

    private BooleanQueryBuilder(final DisjunctionMaxQuery parent, final List<DisjunctionMaxQueryBuilder> clauses) {
        this.parent = parent;
        this.clauses = clauses;
    }

    public BooleanQueryBuilder setParent(final DisjunctionMaxQuery dmq) {
        this.parent = dmq;
        return this;
    }

    @Override
    public BooleanQueryBuilder setAttributesFromObject(BooleanQuery o) {
        return null;
    }

    @Override
    public String getBuilderName() {
        return null;
    }

    @Override
    public DisjunctionMaxClause buildDmc(DisjunctionMaxQuery parent) {
        return null;
    }


    public BooleanQueryBuilder addDmqBuilder(DisjunctionMaxQueryBuilder builder) {
        this.clauses.add(builder);
        return this;
    }

    public List<DisjunctionMaxQueryBuilder> getClauses() {
        return clauses;
    }

    @Override
    public BooleanQuery build() {
//        final BooleanQuery boolq = new BooleanQuery(this.parent, Clause.Occur.SHOULD, false);
//
//        clauses.stream().map(dmq -> dmq.setParent(boolq).build()).forEach(boolq::addClause);
//
//        return boolq;
        return null;
    }

    @Override
    public BooleanQuery build(DisjunctionMaxQuery parent) {
        return null;
    }

    @Override
    public Map<String, Object> attributesToMap() {
        return null;
    }

    @Override
    public BooleanQueryBuilder setAttributesFromMap(Map map) throws QueryBuilderException {
        return null;
    }

    public static BooleanQueryBuilder builder() {
        return builder(null);
    }

    public static BooleanQueryBuilder builder(final DisjunctionMaxQuery parent) {
        return new BooleanQueryBuilder(parent, new LinkedList<>());
    }

    public static BooleanQueryBuilder fromQuery(BooleanQuery booleanQuery) {
        BooleanQueryBuilder builder = builder();

        booleanQuery.getClauses().stream()
                .map(clause -> {

                    if (clause instanceof DisjunctionMaxQuery) {
                        return DisjunctionMaxQueryBuilder.fromQuery((DisjunctionMaxQuery) clause);

                    } else {
                        throw new UnsupportedOperationException("The structure of this query is currently not supported by builders");
                    }})

                .forEach(builder::addDmqBuilder);

        return builder;
    }

    public static BooleanQueryBuilder bool(final DisjunctionMaxQueryBuilder... dmqs) {
        return new BooleanQueryBuilder(null, Arrays.stream(dmqs).collect(Collectors.toList()));
    }

    public static BooleanQueryBuilder bool(final String... dmqs) {
        return new BooleanQueryBuilder(null,
                Arrays.stream(dmqs).map(DisjunctionMaxQueryBuilder::dmq).collect(Collectors.toList()));
    }

    @JsonGetter("clauses")
    public List<Map<String, DisjunctionMaxQueryBuilder>> serializeClauses() {
        return this.clauses.stream()
                .map(clause -> Collections.singletonMap("dmq", clause))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "bool[" +
                String.join(", ", clauses.stream()
                        .map(DisjunctionMaxQueryBuilder::toString).collect(Collectors.toList())) + "]";
    }

    // TODO: equals() also for nodes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanQueryBuilder that = (BooleanQueryBuilder) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(clauses, that.clauses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, clauses);
    }
}
