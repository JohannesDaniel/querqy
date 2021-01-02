package querqy.model.builder;

import java.util.Collections;
import java.util.Map;

public interface QuerqyQueryBuilder<B, O, P> {

    default O build() {
        return build(null);
    }

    O build(final P parent);
    B setAttributesFromObject(final O o);


    default Map<String, Object> toMap() {
        return Collections.singletonMap(getBuilderName(), attributesToMap());
    }

    String getBuilderName();
    Map attributesToMap();

    default B setAttributesFromWrappedMap(final Map map) {
        final Object attributes = map.get(getBuilderName());

        if (attributes instanceof Map) {
            return setAttributesFromMap((Map) attributes);

        } else {
            throw new QueryBuilderException(String.format("Attributes are expected to be wrapped by %s", getBuilderName()));
        }
    }

    B setAttributesFromMap(final Map map);


}
