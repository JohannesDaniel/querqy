package querqy.model.builder.impl;

import org.junit.Test;
import querqy.model.Clause;
import querqy.model.StringRawQuery;
import querqy.model.builder.AbstractBuilderTest;
import querqy.model.builder.model.Occur;

import static org.assertj.core.api.Assertions.assertThat;
import static querqy.model.builder.model.BuilderFieldSettings.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldSettings.OCCUR;
import static querqy.model.builder.model.BuilderFieldSettings.RAW_QUERY;

public class StringRawQueryBuilderTest extends AbstractBuilderTest {

    @Test
    public void testSetAttributesFromMap() {

        assertThat(new StringRawQueryBuilder(
                map(
                        entry(StringRawQueryBuilder.NAME_OF_QUERY_TYPE, map(
                                entry(RAW_QUERY.fieldName, "a"),
                                entry(OCCUR.fieldName, "must"),
                                entry(IS_GENERATED.fieldName, true)))
                )
        )).isEqualTo(new StringRawQueryBuilder("a", Occur.MUST, true));

    }

    @Test
    public void testBuilderToMap() {
        assertThat(new StringRawQueryBuilder("a", Occur.MUST, true).toMap())
                .isEqualTo(
                        map(
                                entry(StringRawQueryBuilder.NAME_OF_QUERY_TYPE,
                                        map(
                                                entry(RAW_QUERY.fieldName, "a"),
                                                entry(OCCUR.fieldName, "must"),
                                                entry(IS_GENERATED.fieldName, true)))
                        )
                );
    }

    @Test
    public void testSetAttributesFromObject() {
        StringRawQueryBuilder stringRawQueryBuilder = new StringRawQueryBuilder("a", Occur.MUST, true);
        StringRawQuery stringRawQuery = new StringRawQuery(null,"a", Clause.Occur.MUST, true);
        assertThat(new StringRawQueryBuilder(stringRawQuery)).isEqualTo(stringRawQueryBuilder);
    }

    @Test
    public void testBuild() {
        StringRawQueryBuilder stringRawQueryBuilder = new StringRawQueryBuilder("a", Occur.MUST, true);
        StringRawQuery stringRawQuery = new StringRawQuery(null,"a", Clause.Occur.MUST, true);
        assertThat(stringRawQueryBuilder.build()).isEqualTo(stringRawQuery);
    }
}
