package querqy.model.builder.converter;

import querqy.model.builder.TypeCastingBuilderUtils;
import querqy.model.builder.QueryNodeBuilder;

import java.util.stream.Collectors;

public class MapValueConverterImpl {

    private MapValueConverterImpl() {}

    public static final MapValueConverter QUERY_NODE_CONVERTER = new QueryNodeConverter();
    public static final MapValueConverter LIST_OF_QUERY_NODE_CONVERTER = new QueryNodeConverter();


    public static class ListOfQueryNodeConverter implements MapValueConverter {
        @Override
        public Object toMapValue(final Object builderValue) {
            return TypeCastingBuilderUtils.castListOfQueryNodeBuilders(builderValue).stream()
                    .map(QueryNodeBuilder::toMap)
                    .collect(Collectors.toList());
        }
    }

    public static class QueryNodeConverter implements MapValueConverter {
        @Override
        public Object toMapValue(final Object builderValue) {
            return TypeCastingBuilderUtils.castQueryNodeBuilder(builderValue).toMap();
        }
    }
}
