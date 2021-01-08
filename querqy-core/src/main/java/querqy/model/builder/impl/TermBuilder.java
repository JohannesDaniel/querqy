package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.ComparableCharSequence;
import querqy.model.DisjunctionMaxClause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.Term;
import querqy.model.builder.TypeCastingUtils;
import querqy.model.builder.DisjunctionMaxClauseBuilder;
import querqy.model.builder.model.BuilderField;

import java.util.Map;

import static querqy.model.builder.model.BuilderFieldSettings.FIELD;
import static querqy.model.builder.model.BuilderFieldSettings.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldSettings.VALUE;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TermBuilder implements DisjunctionMaxClauseBuilder<TermBuilder, Term> {

    public static final String NAME_OF_QUERY_TYPE = "term";

    @BuilderField(settings = VALUE)
    private String value;

    @BuilderField(settings = FIELD, fieldIsMandatory = false)
    private String field;

    @BuilderField(settings = IS_GENERATED)
    private Boolean isGenerated;

    public TermBuilder(final Term term) {
        this.setAttributesFromObject(term);
    }

    public TermBuilder(final Map map) {
        this.fromMap(map);
    }

    public TermBuilder(final String value) {
        this.value = value;
    }

    @Override
    public TermBuilder getBuilder() {
        return this;
    }

    @Override
    public Class<TermBuilder> getBuilderClass() {
        return TermBuilder.class;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public Term buildObject(DisjunctionMaxQuery parent) {
        return new Term(parent, field, value, isGenerated);
    }

    @Override
    public DisjunctionMaxClause buildDisjunctionMaxClause(DisjunctionMaxQuery parent) {
        return build(parent);
    }

    @Override
    public TermBuilder setAttributesFromMap(final Map map) {
        TypeCastingUtils.castString(map.get(VALUE.fieldName)).ifPresent(this::setValue);
        TypeCastingUtils.castString(map.get(FIELD.fieldName)).ifPresent(this::setField);
        TypeCastingUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setIsGenerated);

        return this;
    }

    @Override
    public TermBuilder setAttributesFromObject(final Term term) {
        this.setValue(term.getComparableCharSequence().toString());
        this.setField(term.getField());
        this.setIsGenerated(term.isGenerated());

        return this;
    }

    public static TermBuilder term(final String value, final String field, final Boolean isGenerated) {
        return new TermBuilder(value, field, isGenerated).checkAndSetDefaults();
    }

    public static TermBuilder term(final String value, final boolean isGenerated) {
        return term(value, null, isGenerated);
    }

    public static TermBuilder term(final ComparableCharSequence value) {
        return term(value.toString());
    }

    public static TermBuilder term(final String value) {
        return term(value, null, null);
    }
}
