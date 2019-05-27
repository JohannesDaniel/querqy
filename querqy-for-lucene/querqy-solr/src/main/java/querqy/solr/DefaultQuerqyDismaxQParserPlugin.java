package querqy.solr;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;

import querqy.lucene.rewrite.cache.TermQueryCache;
import querqy.infologging.InfoLogging;

public class DefaultQuerqyDismaxQParserPlugin extends AbstractQuerqyDismaxQParserPlugin {

   @Override
   public QParser createParser(final String qstr, final SolrParams localParams, final SolrParams params,
                               final SolrQueryRequest req, final InfoLogging infoLogging,
                               final TermQueryCache termQueryCache) {
         return new QuerqyDismaxQParser(qstr, localParams, params, req,
                 createQuerqyParser(qstr, localParams, params, req), rewriteChain, infoLogging, termQueryCache);
   }

}
