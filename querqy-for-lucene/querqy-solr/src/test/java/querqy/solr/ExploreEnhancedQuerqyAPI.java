package querqy.solr;

import org.apache.solr.SolrJettyTestBase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.json.DirectJsonQueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QueryParsing;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@SolrTestCaseJ4.SuppressSSL
public class ExploreEnhancedQuerqyAPI extends SolrJettyTestBase {

    @BeforeClass
    public static void beforeTests() throws Exception {
        initCore("solrconfig-external-rewriting.xml", "schema.xml");
        //addDocs();
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
