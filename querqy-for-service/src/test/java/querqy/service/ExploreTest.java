package querqy.service;

import org.apache.solr.EmbeddedSolrServerTestBase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.json.DirectJsonQueryRequest;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.handler.component.QueryComponent;
import org.apache.solr.request.SolrQueryRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SolrTestCaseJ4.SuppressSSL
public class ExploreTest extends EmbeddedSolrServerTestBase {

    @BeforeClass
    public static void beforeTests() throws Exception {
        initCore("solrconfig.xml", "schema.xml");
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

    /**
     * What is needed?
     * - Rewritten query needs to be translated into bool + edismax / querqy queries
     * - Query Parser is per default edismax, can be overwritten?
     * - The first approach considers not to add any local params to nested queries (they should inherit from the outside)
     * -
     *
     * - RewriterFactoryAdapter
     *      -> CommonRulesRewriter
     *      -> ShingleRewriter
     *      -> ReplaceRewriter
     *      -> NumberUnitRewriter -> later
     *
     *
     * - BQ -> BQ
     * - DMQ -> edismax
     * - Boost -> SHOULD clause
     * - Options for relaxation?
     *
     * - class SolrQueryBuilder
     * - qf is mandatory
     * - queryString
     * - addBoostQuery
     * - addFilterQuery
     * - addParam
     *      -> bf and bq should be blocked as global params and instead
     *
     *
     */

    @Test
    public void test7() throws IOException, SolrServerException {
        SolrClient solrClient = super.getSolrClient();
        InputStream is = getClass().getClassLoader().getResourceAsStream("req1.json");
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
    public void test6() throws IOException, SolrServerException {
        SolrClient solrClient = super.getSolrClient();

        List<String> boostQueries = new ArrayList<>();
        boostQueries.add("{!func}if(query({!edismax v=\'schwarz\'}),100,0)");
        boostQueries.add("{!func}field(id)");

        final ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("qf", "f1^10 f2^5");
        solrParams.set("additional_boost_queries", "{!func}0");

        // subqueries with multiple clauses require wrapping functions
        // dmq -> all should -> max function
        // bool -> all max -> div(score, len(clauses))
        Map<String, Object> query =
                bool(
                        must(
                                bool(
                                        should(
                                                bool(
                                                        must(
                                                            edismax("led"),
                                                            edismax("tv")
                                                        )
                                                ),
                                                edismax("television")
                                        )
                                )
                        ),
                        should(
                                // func("field(id)"),
                                param("param1"),


                                param("additional_boost_queries")
                        )
        );


        final JsonQueryRequest jsonQuery = new JsonQueryRequest(solrParams)
                .setQuery(query)
                //.withParam("query", query)
                .withParam("queries",
                        Collections.singletonMap("param1",
                                Collections.singletonMap("func",
                                        Collections.singletonMap("query", "max(10000,20000)"))))

                .withParam("fl", "*,score");

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();

//        jsonQuery.getContentWriter(null).write(stream);
//        System.out.println(new String(stream.toByteArray()));


//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        String line = "Hello there!";
//
//        stream.write(line.getBytes());
//        String finalString = new String(stream.toByteArray());
//
//        System.out.println(finalString);


        final QueryResponse response = jsonQuery.process(solrClient, "collection1");

        for (SolrDocument doc : response.getResults()) {
            System.out.println(doc);
        }

        System.out.println(response);

    }


    @SafeVarargs
    private final Map<String, Object> bool(Map<String, Object>... clauses) {
        final Map<String, Object> boolMap = new HashMap<>();
        for (Map<String, Object> clause : clauses) {
            clause.forEach(boolMap::put);
        }

        return Collections.singletonMap("bool", boolMap);
    }

    @SafeVarargs
    private final Map<String, Object> must(Map<String, Object>... clauses) {
        return Collections.singletonMap("must", Arrays.stream(clauses).collect(Collectors.toList()));
    }

    @SafeVarargs
    private final Map<String, Object> should(Map<String, Object>... clauses) {
        return Collections.singletonMap("should", Arrays.stream(clauses).collect(Collectors.toList()));
    }

    private Map<String, Object> edismax(String query) {
        return Collections.singletonMap("edismax", Collections.singletonMap("query", query));
    }

    private Map<String, Object> edismax(String query, int mm) {
        return Collections.singletonMap("edismax", Collections.singletonMap("query", query));
    }

    private Map<String, Object> func(String query) {
        return Collections.singletonMap("func", Collections.singletonMap("query", query));
    }

    private Map<String, Object> param(String paramVal) {
        return Collections.singletonMap("param", paramVal);
    }

    @Test
    public void test5() throws IOException, SolrServerException {
        SolrClient solrClient = super.getSolrClient();



        List<String> boostQueries = new ArrayList<>();
        boostQueries.add("{!func}if(query({!edismax v=\'schwarz\'}),100,0)");
        boostQueries.add("{!func}field(id)");

        final ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("qf", "f1^10 f2^5");

        Map<String, Object> query = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        query.put("edismax", params);

        params.put("query", "television tv");
        params.put("bq", boostQueries);
        params.put("bf", "if(query({!edismax v=\'schwarz\'}),100,0)");

        // query.put("bq", boostQueries);

        Map<String, Object> filters = new HashMap<>();

        final JsonQueryRequest jsonQuery = new JsonQueryRequest(solrParams)
                .setQuery(query)
                //.withParam("bq", "{!func}if(query({!lucene df=f2 v=\'schwarz\'}),100,0)")
                //.withParam("bq", "{!func}if(query({!lucene df=id v=1}),500,0)")
                //.withParam("bq", "{!func}field(id)")
                // .withParam("querqy_boosts", boostQueries)
                // .withFilter("f2:(schwarz OR blau)")
                // .withFilter("f2:(schwarz OR rot)")
                .withParam("fl", "*,score")
                ;


        final QueryResponse response = jsonQuery.process(solrClient, "collection1");

        for (SolrDocument doc : response.getResults()) {
            System.out.println(doc);
        }

        System.out.println(response);

    }



    @Test
    public void test4() throws IOException, SolrServerException {
        SolrClient solrClient = super.getSolrClient();

        Map<String, Object> query = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        query.put("edismax", params);

        params.put("query", "tv");
        params.put("qf", "f1");

        final JsonQueryRequest jsonQuery = new JsonQueryRequest()
                .setQuery(query)
                .withParam("fl", "*,score")
                ;

        final QueryResponse response = jsonQuery.process(solrClient, "collection1");

        for (SolrDocument doc : response.getResults()) {
            System.out.println(doc);
        }

        System.out.println(response);

    }

    @Test
    public void test3() throws IOException, SolrServerException {
        SolrClient solrClient = super.getSolrClient();

        Map<String, Object> query = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        query.put("func", params);

        params.put("query", "field(id)");

        final JsonQueryRequest jsonQuery = new JsonQueryRequest()
                .setQuery(query)
                .withParam("fl", "*,score")
                ;

        final QueryResponse response = jsonQuery.process(solrClient, "collection1");

        for (SolrDocument doc : response.getResults()) {
            System.out.println(doc);
        }

        System.out.println(response);


    }

    @Test
    public void test2() throws IOException, SolrServerException {
        SolrClient solrClient = super.getSolrClient();



        List<String> boostQueries = new ArrayList<>();
//        boostQueries.add("{!func}if(query({!edismax v=\'schwarz\'}),100,0)");
//        boostQueries.add("{!func}field(id)");

        final ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("qf", "f1^10 f2^5");

        Map<String, Object> query = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        query.put("edismax", params);

        params.put("query", "television tv");
        params.put("bq", boostQueries);

        // query.put("bq", boostQueries);

        Map<String, Object> filters = new HashMap<>();

        final JsonQueryRequest jsonQuery = new JsonQueryRequest(solrParams)
                .setQuery(query)
                //.withParam("bq", "{!func}if(query({!lucene df=f2 v=\'schwarz\'}),100,0)")
                //.withParam("bq", "{!func}if(query({!lucene df=id v=1}),500,0)")
                //.withParam("bq", "{!func}field(id)")
                // .withParam("querqy_boosts", boostQueries)
                //.withFilter("f2:(schwarz OR blau)")
                //.withFilter("f2:(schwarz OR rot)")
                .withParam("fl", "*,score")
                ;


        final QueryResponse response = jsonQuery.process(solrClient, "collection1");

        for (SolrDocument doc : response.getResults()) {
            System.out.println(doc);
        }

        System.out.println(response);

    }

    @Test
    public void test() {
        String q = "television";

        SolrQueryRequest req = req("q", q,
                "sort", "id desc",
                DisMaxParams.QF, "f1^10 f2^5",
                "fl", "id,score",
                DisMaxParams.MM, "100%",
                "uq.similarityScore", "off",
                "debugQuery", "on",
                "defType", "edismax");

        assertQ("",
                req,
                "//result[@name='response' and @numFound='2']",
                "//result[@name='response']/doc[1]/str[@name='id'][text()='13']",
                "//result[@name='response']/doc[1]/float[@name='score'][text()='31.0']",
                "//result[@name='response']/doc[2]/str[@name='id'][text()='12']",
                "//result[@name='response']/doc[2]/float[@name='score'][text()='31.0']"
        );
        req.close();
    }



}
