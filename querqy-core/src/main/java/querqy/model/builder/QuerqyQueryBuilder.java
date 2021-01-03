package querqy.model.builder;

import querqy.model.QuerqyQuery;

public interface QuerqyQueryBuilder<B, O, P> extends QueryNodeBuilder<B, O, P> {

    QuerqyQuery<?> buildQuerqyQuery();

}
