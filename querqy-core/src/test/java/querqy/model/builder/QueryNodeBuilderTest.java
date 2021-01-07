package querqy.model.builder;

import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.BuilderFieldProperties;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldProperties.FILTER_QUERIES;

@RunWith(MockitoJUnitRunner.class)
public class QueryNodeBuilderTest {

    @Test
    public void test() {
        QueryNodeBuilder queryNodeBuilder = mock(QueryNodeBuilderTestDelegate.class, Mockito.CALLS_REAL_METHODS);

        queryNodeBuilder.fromMap(Collections.emptyMap());

    }

    @NoArgsConstructor
    public abstract class QueryNodeBuilderTestDelegate implements QueryNodeBuilder {
        @BuilderField(fieldProperties = BuilderFieldProperties.IS_GENERATED)
        private Boolean isGenerated;

        @BuilderField(fieldProperties = FILTER_QUERIES)
        private List<QueryNodeBuilder> filterQueries;

        @Override
        public Class<QueryNodeBuilderTestDelegate> getBuilderClass() {
            return QueryNodeBuilderTestDelegate.class;
        }

    }


}
