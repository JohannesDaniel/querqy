package querqy.model.builder.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import querqy.model.Term;
import querqy.model.builder.AbstractBuilderTest;
import querqy.model.builder.QueryBuilderException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static querqy.model.builder.model.BuilderFieldSettings.VALUE;
import static querqy.model.builder.model.BuilderFieldSettings.FIELD;
import static querqy.model.builder.model.BuilderFieldSettings.IS_GENERATED;
import static querqy.model.builder.impl.TermBuilder.term;

public class TermBuilderTest extends AbstractBuilderTest {

    @Test
    public void testThatExceptionIsThrownIfValueIsNull() {
        assertThatThrownBy(() -> new TermBuilder(null, null, false).build())
                .isInstanceOf(QueryBuilderException.class);
    }

    @Test
    public void testThatNoExceptionIsThrownIfValueIsNotNull() {
        assertThatCode(() -> new TermBuilder("term", null, false).build()).doesNotThrowAnyException();
    }

    @Test
    public void testSetAttributesFromMap() {
        new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
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
                                                entry(IS_GENERATED.fieldName, true)))
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
