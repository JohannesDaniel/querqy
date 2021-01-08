package querqy.model.builder.converter;

import querqy.model.builder.model.BuilderFieldSettings;

import java.util.LinkedHashMap;

import static java.util.Objects.nonNull;

public class QueryBuilderMap extends LinkedHashMap<String, Object> {

    public void putBuilderField(final BuilderFieldSettings settings, final Object value, final MapSettings mapSettings) {

        super.put(settings.fieldName, settings.mapValueConverter.toMapValue(value));

    }
}
