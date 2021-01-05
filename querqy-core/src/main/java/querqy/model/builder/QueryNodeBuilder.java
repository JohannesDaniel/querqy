package querqy.model.builder;

import java.util.Collections;
import java.util.Map;

public interface QueryNodeBuilder<B extends Object, O, P> {

    void setDefaults();

    default O build() {
        return build(null);
    }

    O build(final P parent);

    B setAttributesFromObject(final O o);


    default Map<String, Object> toMap() {
        setDefaults();
        return Collections.singletonMap(getNameOfQueryType(), attributesToMap());
    }

    String getNameOfQueryType();
    Map<String, Object> attributesToMap();

    default B setAttributesFromWrappedMap(final Map map) {
        final Object attributes = map.get(getNameOfQueryType());

        if (attributes instanceof Map) {
            return setAttributesFromMap((Map) attributes);

        } else {
            throw new QueryBuilderException(String.format("Attributes are expected to be wrapped by %s", getNameOfQueryType()));
        }
    }

    B setAttributesFromMap(final Map map);


}
