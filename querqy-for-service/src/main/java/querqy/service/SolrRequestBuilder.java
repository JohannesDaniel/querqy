package querqy.service;

import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.common.params.MultiMapSolrParams;
import querqy.model.ExpandedQuery;
import querqy.model.MatchAllQuery;
import querqy.model.QuerqyQuery;
import querqy.model.Query;
import querqy.rewrite.commonrules.FieldAwareWhiteSpaceQuerqyParserFactory;
import querqy.rewrite.commonrules.QuerqyParserFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SolrRequestBuilder {

    private final RewritingAdapter rewritingAdapter;
    private final Map<String, List<String>> params;

    private String queryString;
    private final Map<String, Integer> fieldDefinitions;
    private final List<String> boostQueries;
    private final List<String> filterQueries;

    private static final String EMPTY_STRING = "";
    private static final SolrRequestNodeVisitor SOLR_REQUEST_NODE_VISITOR = new SolrRequestNodeVisitor();

    public SolrRequestBuilder(final RewritingAdapter rewritingAdapter) {
        this.rewritingAdapter = rewritingAdapter;

        this.params = new HashMap<>();
        this.fieldDefinitions = new HashMap<>();
        this.boostQueries = new ArrayList<>();
        this.filterQueries = new ArrayList<>();
    }

    public SolrRequestBuilder addBoostQuery(final String boostQuery) {
        this.boostQueries.add(boostQuery);
        return this;
    }

    public SolrRequestBuilder addFilterQuery(final String filterQuery) {
        this.filterQueries.add(filterQuery);
        return this;
    }

    public SolrRequestBuilder addParam(final String key, final String value) {
        // special handling of params bq and qf

        params.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        return this;
    }

    public JsonQueryRequest buildJsonQueryRequest() {
        final Map<String, String[]> requestParams = this.params.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toArray(new String[0])));

        final ExpandedQuery expandedQuery = this.rewritingAdapter.createQuerqyQuery(
                this.queryString != null ? this.queryString : EMPTY_STRING, requestParams);

        final JsonQueryRequest solrRequest = new JsonQueryRequest(new MultiMapSolrParams(requestParams));


        // TBD
        return null;
    }

    private Object createQuery(final ExpandedQuery expandedQuery, final List<String> additionalBoostQueries) {
        final QuerqyQuery<?> querqyQuery = expandedQuery.getUserQuery();

        if (querqyQuery instanceof Query) {
            final List<Object> clauses = SOLR_REQUEST_NODE_VISITOR.visit((Query) querqyQuery);




//            final List<String> newList = Stream.concat(listOne.stream(), listTwo.stream())
//                    .collect(Collectors.toList());


        } else if (querqyQuery instanceof MatchAllQuery){
            final Object clause = SOLR_REQUEST_NODE_VISITOR.visit((MatchAllQuery) querqyQuery).get(0);

        } else {
            // TODO: throw
        }


        return null;
    }



}
