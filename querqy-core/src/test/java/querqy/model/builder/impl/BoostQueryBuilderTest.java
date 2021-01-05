package querqy.model.builder.impl;

import org.junit.Test;
import querqy.model.BoostQuery;
import querqy.model.Clause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.Query;
import querqy.model.Term;
import querqy.model.builder.AbstractBuilderTest;

import static org.assertj.core.api.Assertions.assertThat;
import static querqy.model.builder.impl.BooleanQueryBuilder.bq;
import static querqy.model.builder.impl.BoostQueryBuilder.boost;
import static querqy.model.builder.model.MapField.BOOST;
import static querqy.model.builder.model.MapField.QUERY;

public class BoostQueryBuilderTest extends AbstractBuilderTest {

    @Test
    public void testSetAttributesFromMap() {
        assertThat(new BoostQueryBuilder(
                map(
                        entry(BoostQueryBuilder.NAME_OF_QUERY_TYPE,
                                map(
                                        entry(QUERY.fieldName, bq("a").toMap()),
                                        entry(BOOST.fieldName, 1.0f)))

                ))).isEqualTo(boost(bq("a"), 1.0f));
    }

    @Test
    public void testBuilderToMap() {
        BoostQueryBuilder boostBuilder = boost(bq("a"), 1.0f);

        assertThat(boostBuilder.attributesToMap())
                .isEqualTo(
                        map(
                                entry(QUERY.fieldName, bq("a").toMap()),
                                entry(BOOST.fieldName, 1.0f)));
    }

    @Test
    public void testSetAttributesFromObject() {
        BoostQueryBuilder boostBuilder = boost(bq("a"), 1.0f);

        Query query = new Query();

        DisjunctionMaxQuery dmq1 = new DisjunctionMaxQuery(query, Clause.Occur.SHOULD, false);
        Term term1 = new Term(dmq1, "a");
        dmq1.addClause(term1);
        query.addClause(dmq1);

        assertThat(new BoostQueryBuilder(new BoostQuery(query, 1.0f))).isEqualTo(boostBuilder);
    }

    @Test
    public void testBuild() {
        BoostQueryBuilder boostBuilder = boost(bq("a"), 1.0f);

        Query query = new Query();

        DisjunctionMaxQuery dmq1 = new DisjunctionMaxQuery(query, Clause.Occur.SHOULD, false);
        Term term1 = new Term(dmq1, "a");
        dmq1.addClause(term1);
        query.addClause(dmq1);

        BoostQuery expectedBoostQuery = new BoostQuery(query, 1.0f);
        BoostQuery actualBoostQuery = boostBuilder.build();

        assertThat(actualBoostQuery).isEqualTo(expectedBoostQuery);
    }

}
