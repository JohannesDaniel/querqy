package querqy.service;

import querqy.rewrite.RewriteChain;
import querqy.rewrite.RewriterFactory;
import querqy.rewrite.commonrules.SimpleCommonRulesRewriterFactory;
import querqy.rewrite.commonrules.WhiteSpaceQuerqyParserFactory;
import querqy.rewrite.commonrules.select.ExpressionCriteriaSelectionStrategyFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RewritingAdapter {

    private final List<RewriterFactory> rewriterFactories;
    private final RewriteChain rewriteChain;

    public RewritingAdapter(final List<RewriterFactory> rewriterFactories) {
        this.rewriterFactories = rewriterFactories;
        this.rewriteChain = new RewriteChain(rewriterFactories);
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
            return new RewritingAdapter(Collections.unmodifiableList(this.rewriterFactories));
        }
    }
}
