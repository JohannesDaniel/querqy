package querqy.model.builder.impl;

import lombok.AccessLevel;
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
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.DisjunctionMaxClauseBuilder;
import querqy.model.builder.QueryBuilderException;

import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static querqy.model.builder.model.MapField.FIELD;
import static querqy.model.builder.model.MapField.IS_GENERATED;
import static querqy.model.builder.model.MapField.VALUE;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TermBuilder implements DisjunctionMaxClauseBuilder<TermBuilder, Term> {

    public static final String NAME_OF_QUERY_TYPE = "term";

    private String value;
    private String field;
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
    public void setDefaults() {
        // No defaults are needed to be set here; isGenerated is automatically set to false, field is allowed to be
        // null and value does not have a default
    }

    @Override
    public Term build(final DisjunctionMaxQuery parent) {
        if (isNull(this.value)) {
            throw new QueryBuilderException("The value of a term must not be null");
        }

        return new Term(parent, field, value, isGenerated);
    }

    @Override
    public DisjunctionMaxClause buildDisjunctionMaxClause(DisjunctionMaxQuery parent) {
        return build(parent);
    }

    @Override
    public Map<String, Object> attributesToMap() {
        if (isNull(this.value)) {
            throw new QueryBuilderException("The value of a term must not be null");
        }

        final QueryBuilderMap map = new QueryBuilderMap();
        map.put(VALUE.fieldName, this.value);
        map.putIfNotNull(FIELD.fieldName, this.field);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public TermBuilder setAttributesFromMap(final Map map) {
        BuilderUtils.castString(map.get(VALUE.fieldName)).ifPresent(this::setValue);
        BuilderUtils.castString(map.get(FIELD.fieldName)).ifPresent(this::setField);
        BuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setGenerated);

        return this;
    }

    @Override
    public TermBuilder setAttributesFromObject(final Term term) {
        this.setValue(term.getComparableCharSequence().toString());
        this.setField(term.getField());
        this.setGenerated(term.isGenerated());

        return this;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
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
