package querqy.model.builder.model;

import querqy.model.builder.BuilderUtils;
import querqy.model.builder.QueryNodeBuilder;

import java.util.stream.Collectors;

@FunctionalInterface
// TODO: sonar lint complains
public interface BuilderToMapValueResolver {

    Object toMapValue(final Object builderValue);

    BuilderToMapValueResolver convertListOfQueryNodeBuildersToMap = obj ->
            BuilderUtils.castListOfQueryNodeBuilders(obj).stream().map(QueryNodeBuilder::toMap).collect(Collectors.toList());

}
