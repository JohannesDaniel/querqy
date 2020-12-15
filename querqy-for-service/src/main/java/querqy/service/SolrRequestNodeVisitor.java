package querqy.service;

import querqy.model.AbstractNodeVisitor;
import querqy.model.BooleanClause;
import querqy.model.BooleanQuery;
import querqy.model.DisjunctionMaxClause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.MatchAllQuery;
import querqy.model.Query;
import querqy.model.RawQuery;
import querqy.model.StringRawQuery;
import querqy.model.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SolrRequestNodeVisitor extends AbstractNodeVisitor<List<Object>> {

    private static final String QUERY = "query";
    private static final String EDISMAX = "edismax";
    private static final String BOOL = "bool";
    private static final String MUST = "must";
    private static final String SHOULD = "should";
    private static final String MATCH_ALL_QUERY = "*:*";

    @Override
    public List<Object> visit(final Query query) {
        return this.visit((BooleanQuery) query);
    }

    @Override
    public List<Object> visit(final MatchAllQuery query) {
        return Collections.singletonList(this.edismaxQuery(MATCH_ALL_QUERY));
    }

    @Override
    public List<Object> visit(final DisjunctionMaxQuery disjunctionMaxQuery) {
        final List<String> terms = new ArrayList<>();
        final List<Object> parsedClauses = new ArrayList<>();

        for (final DisjunctionMaxClause clause : disjunctionMaxQuery.getClauses()) {
            if (clause instanceof Term) {
                terms.add(((Term) clause).getValue().toString());

            } else if (clause instanceof BooleanQuery){
                parsedClauses.add(
                        Collections.singletonMap(BOOL,
                                Collections.singletonMap(MUST, clause.accept(this))));

            } else {
                // TODO: Exception
            }
        }

        if (!terms.isEmpty()) {
            parsedClauses.add(this.edismaxQuery(terms));
        }

        return parsedClauses;
    }

    private Map<String, Object> edismaxQuery(final List<String> terms) {
        return terms.size() == 1 ? this.edismaxQuery(terms.get(0)) : this.edismaxQuery(String.join(" ", terms));
    }

    private Map<String, Object> edismaxQuery(final String term) {
        return Collections.singletonMap(EDISMAX, Collections.singletonMap(QUERY, term));
    }

    /*

    a AND (b OR c)
    input: a b
    b => SYN: c

    bool:
      dmq:
        term: a
      dmq:
        term: b
        term: c

    bool:
      must:
        edismax:
          query: a
        edismax
          query: b c

    bool:
      dmq:
        bool:
          dmq:
            term: a
          dmq:
            term: b
        bool:


    bool:
      must:
        bool:
          should:
            edismax:
              query: a b
            edismax:
              query: a c




     */

    @Override
    public List<Object> visit(final BooleanQuery booleanQuery) {
        final List<Object> parsedBooleanClauses = new ArrayList<>();

        for (final BooleanClause clause : booleanQuery.getClauses()) {
            if (clause instanceof DisjunctionMaxQuery) {
                final List<Object> parsedDmqClauses = clause.accept(this);
                parsedBooleanClauses.add(
                        Collections.singletonMap(BOOL,
                                Collections.singletonMap(MUST, parsedDmqClauses)));

            } else {
                // TODO: Exception - not supported
            }
        }

        return parsedBooleanClauses;
    }

    @Override
    public List<Object> visit(final Term term) {
        // TODO: Exception - not supported
        throw new RuntimeException();
    }

    @Override
    public List<Object> visit(final RawQuery rawQuery) {
        if (rawQuery instanceof StringRawQuery) {
            return Collections.singletonList(((StringRawQuery) rawQuery).getQueryString());
        } else {
            // TODO: Exception - not supported
            throw new RuntimeException();
        }
    }
}
