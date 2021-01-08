package querqy.model.builder.converter;

import lombok.Builder;

@Builder
public class MapSettings {

    @Builder.Default
    private boolean excludeDefaultValues = false;

    @Builder.Default
    private boolean convertBooleanToString = false;

    private static final MapSettings DEFAULTS = MapSettings.builder().build();
    public static MapSettings defaults() {
        return DEFAULTS;
    }

}
