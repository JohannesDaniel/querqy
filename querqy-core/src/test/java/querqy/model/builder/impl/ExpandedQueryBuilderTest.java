package querqy.model.builder.impl;

import org.junit.Test;
import querqy.model.ExpandedQuery;
import querqy.model.MatchAllQuery;
import querqy.model.builder.AbstractBuilderTest;
import querqy.model.builder.QueryBuilderException;

import static querqy.model.builder.impl.ExpandedQueryBuilder.expanded;
import static querqy.model.builder.impl.QueryBuilder.query;

public class ExpandedQueryBuilderTest extends AbstractBuilderTest {

    @Test(expected = QueryBuilderException.class)
    public void test() {

        ExpandedQuery expandedQuery = new ExpandedQuery(new MatchAllQuery());

        expanded().setAttributesFromObject(expandedQuery);

    }

    @Test
    public void test2() {

        ExpandedQuery expandedQuery = new ExpandedQuery(query("a").build());

        System.out.println(expanded().setAttributesFromObject(expandedQuery));

    }


}
