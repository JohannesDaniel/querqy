package querqy.model.builder.impl;

import querqy.model.BooleanQuery;
import querqy.model.DisjunctionMaxClause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.QuerqyQuery;
import querqy.model.Query;
import querqy.model.Term;
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.DisjunctionMaxClauseBuilder;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;

import java.util.Map;

import static querqy.model.builder.impl.BooleanQueryBuilder.bq;
import static querqy.model.builder.impl.TermBuilder.term;

public class BuilderFactory {

    private BuilderFactory() {}

    public static QuerqyQueryBuilder createQuerqyQueryBuilderFromObject(final QuerqyQuery querqyQuery) {

        if (querqyQuery instanceof Query) {
            return new BooleanQueryBuilder((Query) querqyQuery);

        } else {
            throw new QueryBuilderException("The structure of this query is currently not supported by builders");
        }
    }

    public static QuerqyQueryBuilder createQuerqyQueryBuilderFromMap(final Map map) {
        final String nameOfQueryType = BuilderUtils.expectMapToContainExactlyOneEntryAndGetKey(map);

        if (BooleanQueryBuilder.NAME_OF_QUERY_TYPE.equals(nameOfQueryType)) {
            return bq(map);

        } else if (false) {
            return null;

        } else {
            throw new QueryBuilderException(String.format("Unexpected name of query type: %s", nameOfQueryType));
        }
    }

    public static DisjunctionMaxClauseBuilder createDisjunctionMaxClauseBuilderFromObject(
            final DisjunctionMaxClause clause) {

        if (clause instanceof Term) {
            return new TermBuilder((Term) clause);

        } else if (clause instanceof BooleanQuery){
            return new BooleanQueryBuilder((BooleanQuery) clause);

        } else {
            throw new QueryBuilderException("The structure of this query is currently not supported by builders");
        }
    }

    public static DisjunctionMaxClauseBuilder createDisjunctionMaxClauseBuilderFromMap(final Map map) {
        final String nameOfQueryType = BuilderUtils.expectMapToContainExactlyOneEntryAndGetKey(map);

        if (TermBuilder.NAME_OF_QUERY_TYPE.equals(nameOfQueryType)) {
            return term(map);

        } else if (BooleanQueryBuilder.NAME_OF_QUERY_TYPE.equals(nameOfQueryType)) {
            return bq(map);

        } else {
            throw new QueryBuilderException(String.format("Unexpected name of query type: %s", nameOfQueryType));
        }
    }



}
