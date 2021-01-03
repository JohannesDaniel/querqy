package querqy.rewrite.commonrules.model;

import org.junit.Test;
import querqy.model.EmptySearchEngineRequestAdapter;
import querqy.model.ExpandedQuery;
import querqy.model.Query;
import querqy.model.builder.impl.BooleanQueryBuilder;
import querqy.model.builder.impl.QueryBuilder;
import querqy.rewrite.commonrules.AbstractCommonRulesTest;
import querqy.rewrite.commonrules.CommonRulesRewriter;

import static org.assertj.core.api.Assertions.assertThat;
import static querqy.model.builder.impl.BooleanQueryBuilder.bq;
import static querqy.model.builder.impl.DisjunctionMaxQueryBuilder.dmq;
import static querqy.model.builder.impl.QueryBuilder.query;
import static querqy.model.builder.impl.TermBuilder.term;

public class DeleteInstructionNestedQueriesTest extends AbstractCommonRulesTest {

    @Test
    public void testDeletionOfNestedBooleanQuery() {
        CommonRulesRewriter rewriter = rewriter(
                rule(input("a", "b"),
                        delete("a", "b"))
        );

        QueryBuilder q =
                query(
                        dmq(
                                term("ab"),
                                BooleanQueryBuilder.bq(
                                        dmq("a"),
                                        dmq("b")
                                )
                        ),
                        dmq("b")
                );

        ExpandedQuery query = new ExpandedQuery(q.build());
        Query rewritten = (Query) rewriter.rewrite(query, new EmptySearchEngineRequestAdapter()).getUserQuery();

        assertThat(QueryBuilder.fromQuery(rewritten)).isEqualTo(query(dmq("ab"), dmq("b")));
    }

    @Test
    public void testThatSequencesAcrossNestedBooleanQueriesAreNotRemoved() {

        CommonRulesRewriter rewriter = rewriter(
            rule(input("a", "b"),
                    delete("a", "b"))
        );

        QueryBuilder q =
                query(
                        dmq(
                                term("a"),
                                BooleanQueryBuilder.bq(
                                        dmq("b")
                                )
                        )
                );

        ExpandedQuery query = new ExpandedQuery(q.build());
        Query rewritten = (Query) rewriter.rewrite(query, new EmptySearchEngineRequestAdapter()).getUserQuery();

        assertThat(QueryBuilder.fromQuery(rewritten)).isEqualTo(q);
    }


    // TODO: move subsequent tests to DeleteInstructionTest as soon as the builder lib is more established

    @Test
    public void testThatDmqContainingTheSameTermMultipleTimesIsFullyRemoved() {
        CommonRulesRewriter rewriter = rewriter(
                rule(input("a"),
                        delete("a"))
        );

        QueryBuilder q = query(dmq("a", "a", "a"));

        ExpandedQuery query = new ExpandedQuery(q.build());
        Query rewritten = (Query) rewriter.rewrite(query, new EmptySearchEngineRequestAdapter()).getUserQuery();

        assertThat(QueryBuilder.fromQuery(rewritten)).isEqualTo(query());
    }

    @Test
    public void testThatMultipleDmqsContainingTheSameTermAreFullyRemoved() {
        CommonRulesRewriter rewriter = rewriter(
                rule(input("a"),
                        delete("a"))
        );

        QueryBuilder q = query("a", "a", "a");

        ExpandedQuery query = new ExpandedQuery(q.build());
        Query rewritten = (Query) rewriter.rewrite(query, new EmptySearchEngineRequestAdapter()).getUserQuery();

        assertThat(QueryBuilder.fromQuery(rewritten)).isEqualTo(query());
    }


    // TODO: move subsequent tests as soon as the builder lib is more established

    @Test
    public void testThatSynonymsAreAppliedOnDuplicateSequences() {
        CommonRulesRewriter rewriter = rewriter(
                rule(input("a", "b"),
                        synonym("c"))
        );

        QueryBuilder q = query("a", "b", "a", "b");

        ExpandedQuery query = new ExpandedQuery(q.build());
        Query rewritten = (Query) rewriter.rewrite(query, new EmptySearchEngineRequestAdapter()).getUserQuery();

        assertThat(QueryBuilder.fromQuery(rewritten))
                .isEqualTo(
                        query(
                            dmq(term("a"), term("c", true)),
                            dmq(term("b"), term("c", true)),
                            dmq(term("a"), term("c", true)),
                            dmq(term("b"), term("c", true))));
    }
}
