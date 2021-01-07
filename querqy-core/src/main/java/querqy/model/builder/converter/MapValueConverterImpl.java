package querqy.model.builder.converter;

import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.TypeCastingBuilderUtils;
import querqy.model.builder.QueryNodeBuilder;
import querqy.model.builder.model.Occur;

import java.util.Optional;
import java.util.stream.Collectors;

public class MapValueConverterImpl {

    private MapValueConverterImpl() {}

    public static final MapValueConverter QUERY_NODE_CONVERTER = obj ->
            TypeCastingBuilderUtils.castQueryNodeBuilder(obj).toMap();

    public static final MapValueConverter LIST_OF_QUERY_NODE_CONVERTER = obj ->
            TypeCastingBuilderUtils.castListOfQueryNodeBuilders(obj).stream()
                    .map(QueryNodeBuilder::toMap)
                    .collect(Collectors.toList());

    public static final MapValueConverter OCCUR_CONVERTER = obj -> ((Occur) obj).typeName;

    public static final MapValueConverter FLOAT_CONVERTER = obj -> {
        final Optional<Float> optionalFloat = TypeCastingBuilderUtils.castFloatOrDoubleToFloat(obj);

        if (optionalFloat.isPresent()) {
            return optionalFloat.get();
        } else {
            throw new QueryBuilderException(String.format("Float value expected: %s", obj.toString()));
        }
    };
}
