package querqy.model.builder;

import querqy.ComparableCharSequence;
import querqy.ComparableCharSequenceWrapper;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.Term;

import java.util.Objects;

public class TermBuilder implements DisjunctionMaxClauseBuilder {

    private DisjunctionMaxQuery parent;

    private final ComparableCharSequence seq;
    private final String field;
    private final boolean isGenerated;

    private TermBuilder(final DisjunctionMaxQuery parent,
                        final ComparableCharSequence seq,
                        final String field,
                        final boolean isGenerated) {
        this.parent = parent;
        this.seq = seq;
        this.field = field;
        this.isGenerated = isGenerated;
    }

    @Override
    public TermBuilder setParent(final DisjunctionMaxQuery dmq) {
        this.parent = dmq;
        return this;
    }

    public Term build() {
        return new Term(this.parent, field, seq, isGenerated);
    }

    public static TermBuilder fromQuery(Term term) {
        return term(
                term.getComparableCharSequence(),
                term.getField(),
                term.isGenerated()
        );
    }

    public static TermBuilder term(final ComparableCharSequence seq, final String field, final boolean isGenerated) {
        return new TermBuilder(null, seq, field, isGenerated);
    }

    public static TermBuilder term(final String seq, final String field, final boolean isGenerated) {
        return term(new ComparableCharSequenceWrapper(seq), field, isGenerated);
    }

    public static TermBuilder term(final String seq, final boolean isGenerated) {
        return term(new ComparableCharSequenceWrapper(seq), null, isGenerated);
    }

    public static TermBuilder term(final ComparableCharSequence seq) {
        return term(seq, null, false);
    }

    public static TermBuilder term(final String seq) {
        return term(new ComparableCharSequenceWrapper(seq), null, false);
    }


    @Override
    public String toString() {
        return seq.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermBuilder that = (TermBuilder) o;
        return isGenerated == that.isGenerated &&
                Objects.equals(parent, that.parent) &&
                Objects.equals(seq, that.seq) &&
                Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, seq, field, isGenerated);
    }
}
