package querqy.model.builder.impl;

import org.junit.Test;
import querqy.model.Clause;
import querqy.model.MatchAllQuery;
import querqy.model.builder.AbstractBuilderTest;
import querqy.model.builder.model.Occur;

import static org.assertj.core.api.Assertions.assertThat;
import static querqy.model.builder.impl.MatchAllQueryBuilder.matchall;
import static querqy.model.builder.model.BuilderFieldSettings.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldSettings.OCCUR;

public class MatchAllQueryBuilderTest extends AbstractBuilderTest {

    @Test
    public void testSetAttributesFromMap() {
        assertThat(new MatchAllQueryBuilder(
                map(
                        entry(MatchAllQueryBuilder.NAME_OF_QUERY_TYPE,
                                map(
                                        entry(OCCUR.fieldName, "must"),
                                        entry(IS_GENERATED.fieldName, true)
                                )
                        )
                )


        )).isEqualTo(matchall(Occur.MUST, true));
    }

    @Test
    public void testBuilderToMap() {
        assertThat(matchall(Occur.MUST, true).toMap())
                .isEqualTo(
                        map(
                                entry(MatchAllQueryBuilder.NAME_OF_QUERY_TYPE,
                                        map(
                                                entry(OCCUR.fieldName, "must"),
                                                entry(IS_GENERATED.fieldName, true)
                                        )
                                )
                        )
                );
    }

    @Test
    public void testSetAttributesFromObject() {
        MatchAllQueryBuilder matchAllQueryBuilder = matchall(Occur.MUST, true);

        MatchAllQuery matchAllQuery = new MatchAllQuery(null, Clause.Occur.MUST, true);
        assertThat(new MatchAllQueryBuilder(matchAllQuery)).isEqualTo(matchAllQueryBuilder);
    }

    @Test
    public void testBuild() {
        MatchAllQueryBuilder matchAllQueryBuilder = matchall(Occur.MUST, true);

        MatchAllQuery matchAllQuery = new MatchAllQuery(null, Clause.Occur.MUST, true);

        assertThat(matchAllQueryBuilder.build()).isEqualTo(matchAllQuery);
    }
}
