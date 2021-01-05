package querqy.solr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.solr.SolrJettyTestBase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.json.DirectJsonQueryRequest;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.BeforeClass;
import org.junit.Test;
import querqy.model.builder.impl.BooleanQueryBuilder;
import querqy.model.builder.impl.DisjunctionMaxQueryBuilder;
import querqy.model.builder.impl.ExpandedQueryBuilder;
import querqy.model.builder.impl.TermBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static querqy.model.builder.impl.BooleanQueryBuilder.bq;
import static querqy.model.builder.impl.DisjunctionMaxQueryBuilder.dmq;
import static querqy.model.builder.impl.ExpandedQueryBuilder.expanded;
import static querqy.model.builder.impl.TermBuilder.term;
import static querqy.model.builder.model.Occur.MUST;


@SolrTestCaseJ4.SuppressSSL
@Deprecated
public class ExploreEnhancedQuerqyAPI extends SolrJettyTestBase {

    @BeforeClass
    public static void beforeTests() throws Exception {
        initCore("solrconfig-external-rewriting.xml", "schema.xml");
        addDocs();
    }

    private static void addDocs() {
        assertU(adoc("id", "0", "f1", "tv", "f2", "television"));
        assertU(adoc("id", "1", "f1", "tv"));
        assertU(adoc("id", "2", "f1", "tv", "f2", "led"));
        assertU(adoc("id", "11", "f1", "led tv", "f2", "television"));
        assertU(adoc("id", "12", "f1", "led +tv&", "f2", "television"));
        assertU(adoc("id", "20", "f1", "television", "f2", "schwarz"));
        assertU(adoc("id", "21", "f1", "television", "f2", "blau"));
        assertU(adoc("id", "22", "f1", "television", "f2", "rot"));
        assertU(adoc("id", "30", "f1", "smartphone"));

        assertU(commit());
    }

    @Test
    public void test() {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void test11Query() throws IOException, SolrServerException {
        BooleanQueryBuilder query = bq(
                dmq("tv"),
                dmq(
                        term("smartphone"),
                        bq(
                                dmq("television").setOccur(MUST),
                                dmq("schwarz").setOccur(MUST)
                        )
                )
        );

//        BooleanQueryBuilder query = bq(
//                dmq("tv"),
//                dmq(term("b"), bq("c", "d")));
//

        ExpandedQueryBuilder expandedQuery = expanded(query);

        Map<String, Object> request = new HashMap<>();

        request.put("query", expandedQuery.toMap());
        request.put("mm", "0%");
        request.put("omitHeader", "true");
        request.put("tie", 0.0f);
        request.put("uq.similarityScore", "off");
        request.put("qf", "f1^30 f2^10");

//        System.out.println(objectWriter.writeValueAsString(Collections.singletonMap("query",
//                Collections.singletonMap("querqy", request))));


        SolrClient solrClient = super.getSolrClient();

        final ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("fl", "*,score");
        params.add("facet", "true");
        params.add("facet.field", "f2");

        final JsonQueryRequest jsonQuery = new JsonQueryRequest(params)
                .setQuery(Collections.singletonMap("querqy", request));


        final QueryResponse response = jsonQuery.process(solrClient, "collection1");

        for (SolrDocument doc : response.getResults()) {
            System.out.println(doc);
        }

        System.out.println(response.getFacetFields());


    }

    @Test
    public void test10Dmq() throws IOException, SolrServerException {
        ObjectWriter objectWriter = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writerWithDefaultPrettyPrinter();

        DisjunctionMaxQueryBuilder query = dmq("a");
        // DisjunctionMaxQueryBuilder query = dmq("a", "b");
        // QuerqyQuery querqyQuery = new QuerqyQuery(query, "100%", 0.0f);
        // System.out.println(query);
        // System.out.println(objectWriter.writeValueAsString(querqyQuery));

        System.out.println(objectWriter.writeValueAsString(query.toMap()));

        Map<String, Object> request = new HashMap<>();

        request.put("clauses", query.toMap());
        request.put("mm", "100%");
        request.put("tie", 0.0f);

        System.out.println(objectWriter.writeValueAsString(Collections.singletonMap("query",
                Collections.singletonMap("querqy", request))));


        SolrClient solrClient = super.getSolrClient();

        DirectJsonQueryRequest jsonQuery = new DirectJsonQueryRequest(
                objectWriter.writeValueAsString(
                        Collections.singletonMap("query",
                                Collections.singletonMap("querqy", request))));

        final QueryResponse response = jsonQuery.process(solrClient, "collection1");
    }

    @Test
    public void test9Term() throws IOException, SolrServerException {
        ObjectWriter objectWriter = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writerWithDefaultPrettyPrinter();

        TermBuilder query = term("a");
        // DisjunctionMaxQueryBuilder query = dmq("a", "b");
        // QuerqyQuery querqyQuery = new QuerqyQuery(query, "100%", 0.0f);
        // System.out.println(query);
        // System.out.println(objectWriter.writeValueAsString(querqyQuery));

        System.out.println(objectWriter.writeValueAsString(query.toMap()));

        Map<String, Object> request = new HashMap<>();

        request.put("clauses", query.toMap());
        request.put("mm", "100%");
        request.put("tie", 0.0f);

        System.out.println(objectWriter.writeValueAsString(Collections.singletonMap("query",
                Collections.singletonMap("querqy", request))));


        SolrClient solrClient = super.getSolrClient();

        DirectJsonQueryRequest jsonQuery = new DirectJsonQueryRequest(
                objectWriter.writeValueAsString(
                        Collections.singletonMap("query",
                                Collections.singletonMap("querqy", request))));

        // System.out.println(req);

        final QueryResponse response = jsonQuery.process(solrClient, "collection1");



    }


}
