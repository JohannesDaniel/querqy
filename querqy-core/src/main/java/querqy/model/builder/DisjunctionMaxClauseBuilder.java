package querqy.model.builder;

import querqy.model.DisjunctionMaxClause;
import querqy.model.DisjunctionMaxQuery;

public interface DisjunctionMaxClauseBuilder<B, O extends DisjunctionMaxClause>
        extends QueryNodeBuilder<B, O, DisjunctionMaxQuery> {

    default DisjunctionMaxClause buildDisjunctionMaxClause(final DisjunctionMaxQuery parent) {
        return build(parent);
    }
}
