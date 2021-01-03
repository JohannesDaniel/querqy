package querqy.model.builder;

@Deprecated
public interface TopLevelNodeBuilder<B, O> extends QueryNodeBuilder<B, O, Object> {

    @Override
    default O build(Object parent) {
        throw new UnsupportedOperationException("Not allowed to set parent Node for this QuerqyQueryBuilder");
    }

    @Override
    O build();
}
