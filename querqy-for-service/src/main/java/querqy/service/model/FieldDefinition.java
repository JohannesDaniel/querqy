package querqy.service.model;

public class FieldDefinition {
    public final String fieldName;
    public final int fieldWeight;

    public FieldDefinition(final String fieldName) {
        this(fieldName, 1);
    }

    public FieldDefinition(final String fieldName, final int fieldWeight) {
        this.fieldName = fieldName;
        this.fieldWeight = fieldWeight;
    }
}
