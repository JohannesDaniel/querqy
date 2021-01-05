package querqy.model.builder;

import querqy.model.QuerqyQuery;

public interface QuerqyQueryBuilder<B, O extends QuerqyQuery, P> extends QueryNodeBuilder<B, O, P> {

    default QuerqyQuery<?> buildQuerqyQuery() {
        return build();
    }

}
