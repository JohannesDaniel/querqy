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
import querqy.model.builder.TypeCastingBuilderUtils;
import querqy.model.builder.DisjunctionMaxClauseBuilder;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.QueryBuilderMap;

import java.util.Map;

import static querqy.model.builder.model.BuilderFieldProperties.FIELD;
import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldProperties.VALUE;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TermBuilder implements DisjunctionMaxClauseBuilder<TermBuilder, Term> {

    public static final String NAME_OF_QUERY_TYPE = "term";

    @BuilderField(fieldProperties = VALUE)
    private String value;

    @BuilderField(fieldProperties = FIELD, fieldIsMandatory = false)
    private String field;

    @BuilderField(fieldProperties = IS_GENERATED)
    private boolean isGenerated;

    public TermBuilder(final Term term) {
        this.setAttributesFromObject(term);
    }

    public TermBuilder(final Map map) {
        this.setAttributesFromWrappedMap(map);
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
    public Map<String, Object> attributesToMap() {
        final QueryBuilderMap map = new QueryBuilderMap();
        map.put(VALUE.fieldName, this.value);
        map.putIfNotNull(FIELD.fieldName, this.field);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public TermBuilder setAttributesFromMap(final Map map) {
        TypeCastingBuilderUtils.castString(map.get(VALUE.fieldName)).ifPresent(this::setValue);
        TypeCastingBuilderUtils.castString(map.get(FIELD.fieldName)).ifPresent(this::setField);
        TypeCastingBuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setGenerated);

        return this;
    }

    @Override
    public TermBuilder setAttributesFromObject(final Term term) {
        this.setValue(term.getComparableCharSequence().toString());
        this.setField(term.getField());
        this.setGenerated(term.isGenerated());

        return this;
    }

    public static TermBuilder term(final String value, final String field, final boolean isGenerated) {
        return new TermBuilder(value, field, isGenerated);
    }

    public static TermBuilder term(final String value, final boolean isGenerated) {
        return new TermBuilder(value, null, isGenerated);
    }

    public static TermBuilder term(final ComparableCharSequence value) {
        return new TermBuilder(value.toString());
    }

    public static TermBuilder term(final String value) {
        return new TermBuilder(value);
    }

    public static TermBuilder term(final Map map) {
        return new TermBuilder(map);
    }

    public static TermBuilder term(final Term term) {
        return new TermBuilder(term);
    }
}
