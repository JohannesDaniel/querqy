package querqy.model.builder;

import querqy.model.QuerqyQuery;

public interface QuerqyQueryBuilder<B extends QueryNodeBuilder, O extends QuerqyQuery, P> extends QueryNodeBuilder<B, O, P> {

    default QuerqyQuery<?> buildQuerqyQuery() {
        return build();
    }

}
