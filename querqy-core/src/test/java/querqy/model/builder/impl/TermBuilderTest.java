package querqy.model.builder.impl;

import org.junit.Test;
import querqy.model.Term;
import querqy.model.builder.AbstractBuilderTest;

import static org.assertj.core.api.Assertions.assertThat;
import static querqy.model.builder.model.BuilderFieldProperties.VALUE;
import static querqy.model.builder.model.BuilderFieldProperties.FIELD;
import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.impl.TermBuilder.term;

public class TermBuilderTest extends AbstractBuilderTest {

    @Test
    public void testSetAttributesFromMap() {

        assertThat(new TermBuilder(
                map(
                        entry(TermBuilder.NAME_OF_QUERY_TYPE, map(
                                entry(VALUE.fieldName, "value"),
                                entry(FIELD.fieldName, "field"),
                                entry(IS_GENERATED.fieldName, true)))
                )
        )).isEqualTo(term("value", "field", true));

    }

    @Test
    public void testBuilderToMap() {
        assertThat(term("value", "field", true).toMap())
                .isEqualTo(
                        map(
                                entry(TermBuilder.NAME_OF_QUERY_TYPE,
                                        map(
                                                entry(VALUE.fieldName, "value"),
                                                entry(FIELD.fieldName, "field"),
                                                entry(IS_GENERATED.fieldName, "true")))
                        )
                );
    }

    @Test
    public void testSetAttributesFromObject() {
        TermBuilder termBuilder = term("a");
        Term term = new Term(null, "a");
        assertThat(new TermBuilder(term)).isEqualTo(termBuilder);
    }

    @Test
    public void testBuild() {
        TermBuilder termBuilder = term("a");
        Term term = new Term(null, "a");
        assertThat(termBuilder.build()).isEqualTo(term);
    }
}
