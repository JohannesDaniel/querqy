package querqy.solr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.solr.SolrJettyTestBase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.json.DirectJsonQueryRequest;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QueryParsing;
import org.junit.BeforeClass;
import org.junit.Test;
import querqy.model.builder.impl.BooleanQueryBuilder;
import querqy.model.builder.impl.DisjunctionMaxQueryBuilder;
import querqy.model.builder.impl.ExpandedQueryBuilder;
import querqy.model.builder.impl.QueryBuilder;
import querqy.model.builder.impl.TermBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static querqy.model.builder.impl.BooleanQueryBuilder.bq;
import static querqy.model.builder.impl.DisjunctionMaxQueryBuilder.dmq;
import static querqy.model.builder.impl.ExpandedQueryBuilder.expanded;
import static querqy.model.builder.impl.QueryBuilder.query;
import static querqy.model.builder.impl.TermBuilder.term;
import static querqy.model.builder.model.Occur.MUST;
import static querqy.model.builder.model.Occur.SHOULD;


@SolrTestCaseJ4.SuppressSSL
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


    @Test
    public void test8() throws IOException, SolrServerException {
        ObjectWriter objectWriter = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writerWithDefaultPrettyPrinter();

        QueryBuilder query = query(dmq("a", "b"), dmq(term("c"), bq("d", "e")));
        // QuerqyQuery querqyQuery = new QuerqyQuery(query, "100%", 0.0f);
        // System.out.println(query);
        // System.out.println(objectWriter.writeValueAsString(querqyQuery));


        Map<String, Object> request = new HashMap<>();

        request.put("clauses", query);
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


    @Test
    public void test7() throws IOException, SolrServerException {
        SolrClient solrClient = super.getSolrClient();
        InputStream is = getClass().getClassLoader().getResourceAsStream("req3.json");
        String req = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

        DirectJsonQueryRequest jsonQuery = new DirectJsonQueryRequest(req);

        // System.out.println(req);

        final QueryResponse response = jsonQuery.process(solrClient, "collection1");

        for (SolrDocument doc : response.getResults()) {
            System.out.println(doc);
        }

        System.out.println(response);



    }


    @Test
    public void testRequest() {


        String q = "a k";

        SolrQueryRequest req = req("q", q,
                DisMaxParams.QF, "f1 f2 f3",
                DisMaxParams.MM, "1",
                QueryParsing.OP, "OR",
                "defType", "querqy"
        );

        assertQ("Solr filter query fails",
                req,
                "//result[@name='response' and @numFound='1']"

        );

        req.close();


    }

    @Test
    public void testSolrFilterQuery() {

        String q = "";

        SolrQueryRequest req = req("q", q,
                DisMaxParams.QF, "f1 f2 f3",
                DisMaxParams.MM, "1",
                QueryParsing.OP, "OR",
                "defType", "querqy"
        );

        assertQ("Solr filter query fails",
                req,
                "//result[@name='response' and @numFound='1']"

        );

        req.close();


    }

}
