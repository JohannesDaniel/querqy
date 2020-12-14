package querqy.service;

import querqy.infologging.InfoLoggingContext;
import querqy.rewrite.RewriteChain;
import querqy.rewrite.SearchEngineRequestAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExternalSearchEngineRequestAdapter implements SearchEngineRequestAdapter {
    private final RewriteChain rewriteChain;
    private final Map<String, String[]> params;
    private final Map<String, Object> context;

    public ExternalSearchEngineRequestAdapter(final RewriteChain rewriteChain, final Map<String, String[]> params) {
        this.rewriteChain = rewriteChain;
        this.params = params;
        this.context = new HashMap<>();
    }

    @Override
    public RewriteChain getRewriteChain() {
        return rewriteChain;
    }

    @Override
    public Map<String, Object> getContext() {
        return context;
    }

    @Override
    public Optional<String> getRequestParam(String name) {
        final String[] paramValues = params.get(name);
        return paramValues != null && paramValues.length > 0 ? Optional.ofNullable(params.get(name)[0]) : Optional.empty();
    }

    @Override
    public String[] getRequestParams(String name) {
        final String[] paramValues = params.get(name);
        return paramValues != null ? paramValues : new String[0];
    }

    @Override
    public Optional<Boolean> getBooleanRequestParam(String name) {
        final String[] param = params.get(name);
        return param != null && param.length > 0 ? Optional.of(Boolean.parseBoolean(param[0])) : Optional.empty();
    }

    @Override
    public Optional<Integer> getIntegerRequestParam(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Float> getFloatRequestParam(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Double> getDoubleRequestParam(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<InfoLoggingContext> getInfoLoggingContext() {
        return Optional.empty();
    }

    @Override
    public boolean isDebugQuery() {
        return false;
    }
}
