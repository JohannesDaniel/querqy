package querqy.service;

import querqy.model.ExpandedQuery;
import querqy.parser.QuerqyParser;
import querqy.rewrite.RewriteChain;
import querqy.rewrite.RewriterFactory;
import querqy.rewrite.commonrules.FieldAwareWhiteSpaceQuerqyParserFactory;
import querqy.rewrite.commonrules.QuerqyParserFactory;
import querqy.rewrite.commonrules.SimpleCommonRulesRewriterFactory;
import querqy.rewrite.commonrules.WhiteSpaceQuerqyParserFactory;
import querqy.rewrite.commonrules.select.ExpressionCriteriaSelectionStrategyFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RewritingAdapter {

    private final RewriteChain rewriteChain;

    private static final QuerqyParserFactory QUERQY_PARSER_FACTORY = new FieldAwareWhiteSpaceQuerqyParserFactory();

    private RewritingAdapter(final RewriteChain rewriteChain) {
        this.rewriteChain = rewriteChain;
    }

    public ExpandedQuery createQuerqyQuery(final String queryString, final Map<String, String[]> params) {
        final QuerqyParser querqyParser = QUERQY_PARSER_FACTORY.createParser();

        final ExpandedQuery inputQuery = new ExpandedQuery(querqyParser.parse(queryString));

        return this.rewriteChain.rewrite(inputQuery, new ExternalSearchEngineRequestAdapter(this.rewriteChain, params));
    }

    public Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<RewriterFactory> rewriterFactories = new LinkedList<>();
        private int rewriterIdCounter = 0;

        public RewritingAdapter.Builder addCommonRulesRewriter(final String config) throws IOException {
            final String rewriterId = "querqy_commonrules_" + this.rewriterIdCounter++;
            rewriterFactories.add(new SimpleCommonRulesRewriterFactory(
                    rewriterId,
                    new StringReader(config),
                    new WhiteSpaceQuerqyParserFactory(),
                    true,
                    Collections.emptyMap(),
                    new ExpressionCriteriaSelectionStrategyFactory()));

            return this;
        }

        public RewritingAdapter build() {
            return new RewritingAdapter(new RewriteChain(Collections.unmodifiableList(this.rewriterFactories)));
        }
    }
}
