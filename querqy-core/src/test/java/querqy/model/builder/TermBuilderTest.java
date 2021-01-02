package querqy.model.builder;

import org.junit.Test;
import querqy.model.Term;

import static org.assertj.core.api.Assertions.assertThat;
import static querqy.model.builder.model.MapField.VALUE;
import static querqy.model.builder.model.MapField.FIELD;
import static querqy.model.builder.model.MapField.IS_GENERATED;
import static querqy.model.builder.TermBuilder.term;

public class TermBuilderTest extends AbstractBuilderTest {

    @Test
    public void testSetAttributesFromMap() throws QueryBuilderException {

        assertThat(term().setAttributesFromMap(
                map(
                        entry(VALUE.fieldName, "value"),
                        entry(FIELD.fieldName, "field"),
                        entry(IS_GENERATED.fieldName, true))))
                .isEqualTo(term("value", "field", true));

    }

    @Test
    public void testBuilderToMap() {
        assertThat(term("value", "field", true).attributesToMap())
                .isEqualTo(
                        map(
                                entry(VALUE.fieldName, "value"),
                                entry(FIELD.fieldName, "field"),
                                entry(IS_GENERATED.fieldName, "true")));
    }

    @Test
    public void testBuilderToTerm() {
        TermBuilder termBuilder = term("a");
        Term term = new Term(null, "a");
        assertThat(term).isEqualTo(termBuilder.build());
    }

    @Test
    public void testTermToBuilder() {
        TermBuilder termBuilder = term("a");
        Term term = new Term(null, "a");
        assertThat(termBuilder).isEqualTo(TermBuilder.fromQuery(term));
    }

}
