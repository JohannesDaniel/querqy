package querqy.model.builder;

import querqy.model.DisjunctionMaxClause;
import querqy.model.DisjunctionMaxQuery;

public interface DisjunctionMaxClauseBuilder<B, O> extends QuerqyQueryBuilder<B, O, DisjunctionMaxQuery> {

    DisjunctionMaxClause buildDmc(final DisjunctionMaxQuery parent);
}
