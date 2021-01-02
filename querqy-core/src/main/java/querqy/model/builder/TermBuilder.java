package querqy.model.builder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.ComparableCharSequence;
import querqy.model.DisjunctionMaxClause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.Term;
import querqy.model.builder.BuilderUtils.QueryBuilderMap;

import java.util.Map;
import java.util.Objects;

import static querqy.model.builder.model.MapField.FIELD;
import static querqy.model.builder.model.MapField.IS_GENERATED;
import static querqy.model.builder.model.MapField.VALUE;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class TermBuilder implements DisjunctionMaxClauseBuilder<TermBuilder, Term> {

    public static final String TYPE_NAME = "term";

    private String value;
    private String field;
    private boolean isGenerated;

    @Override
    public Term build(final DisjunctionMaxQuery parent) {
        Objects.requireNonNull(this.value, "The value of a term must not be null");
        return new Term(parent, field, value, isGenerated);
    }

    @Override
    public DisjunctionMaxClause buildDmc(DisjunctionMaxQuery parent) {
        return build(parent);
    }

    @Override
    public Map<String, Object> attributesToMap() {
        Objects.requireNonNull(this.value, "The value of a term must not be null");

        final QueryBuilderMap map = new QueryBuilderMap();
        map.put(VALUE.fieldName, this.value);
        map.putIfNotNull(FIELD.fieldName, this.field);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public TermBuilder setAttributesFromMap(final Map map) throws QueryBuilderException {
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
    public String getBuilderName() {
        return TYPE_NAME;
    }

//    public static TermBuilder fromMap(final Map<String, Object> map) {
//        // final Object
//
//
//
//        return term().setAttributesFromMap(map);
//    }

    public static TermBuilder fromQuery(final Term term) {
        return term().setAttributesFromObject(term);
    }

    public static TermBuilder term(final String value, final String field, final boolean isGenerated) {
        return new TermBuilder(value, field, isGenerated);
    }

    public static TermBuilder term(final String value, final boolean isGenerated) {
        return term(value, null, isGenerated);
    }

    public static TermBuilder term(final ComparableCharSequence value) {
        return term(value.toString(), null, false);
    }

    public static TermBuilder term(final String value) {
        return term(value, null, false);
    }

    public static TermBuilder term() {
        return new TermBuilder();
    }
}
