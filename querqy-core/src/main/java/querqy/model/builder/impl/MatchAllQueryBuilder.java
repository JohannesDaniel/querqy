package querqy.model.builder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import querqy.model.Clause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.MatchAllQuery;
import querqy.model.QuerqyQuery;
import querqy.model.builder.BuilderUtils;
import querqy.model.builder.QuerqyQueryBuilder;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.Occur;

import java.lang.reflect.Field;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static querqy.model.builder.model.MapField.IS_GENERATED;
import static querqy.model.builder.model.MapField.OCCUR;
import static querqy.model.builder.model.Occur.SHOULD;
import static querqy.model.builder.model.Occur.getOccurByClauseObject;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MatchAllQueryBuilder implements QuerqyQueryBuilder<MatchAllQueryBuilder, MatchAllQuery, DisjunctionMaxQuery> {

    public static final String NAME_OF_QUERY_TYPE = "match_all_query";

    @BuilderField(field = IS_GENERATED)
    private Boolean isGenerated;

    @BuilderField(field = OCCUR)
    private Occur occur;

    public MatchAllQueryBuilder(final MatchAllQuery matchAllQuery) {
        this.setAttributesFromObject(matchAllQuery);
    }

    public MatchAllQueryBuilder(final Map map) {
        this.setAttributesFromWrappedMap(map);
    }


    @Override
    public QuerqyQuery<?> buildQuerqyQuery() {
        return new MatchAllQuery(isGenerated);
    }

    @Override
    public void setDefaults() {
        final Field[] fields = MatchAllQueryBuilder.class.getDeclaredFields();

        for (final Field field : fields) {
            final BuilderField fieldAnnotation = field.getAnnotation(BuilderField.class);

            if (nonNull(fieldAnnotation)) {
                try {
                    final Object fieldValue = field.get(this);
                    if (isNull(fieldValue)) {


                    }


                } catch (IllegalAccessException e) {
                    throw new QueryBuilderException(
                            String.format("Error happened when setting defaults for field %s", field.getName()), e);
                }
            }

            // Field has annotation?
            // Field is set?
            // Field has is mandatory / has default?
            // Set default / throw


            System.out.println(field.getName());
            try {
                System.out.println(field.get(this));
                System.out.println(field.getAnnotatedType());
                System.out.println(field.get(this));
                System.out.println(field.getAnnotation(BuilderField.class));
                // field.set(this, "ab");

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.println();
        }

        if (isNull(this.occur)) {
            this.setOccur(SHOULD);
        }
    }

    @Override
    public MatchAllQuery build(final DisjunctionMaxQuery parent) {
        setDefaults();
        return new MatchAllQuery(parent, Clause.Occur.SHOULD, isGenerated);
    }

    @Override
    public MatchAllQueryBuilder setAttributesFromObject(MatchAllQuery matchAllQuery) {
        this.setOccur(getOccurByClauseObject(matchAllQuery.getOccur()));
        this.setIsGenerated(matchAllQuery.isGenerated());
        return this;
    }

    @Override
    public String getNameOfQueryType() {
        return NAME_OF_QUERY_TYPE;
    }

    @Override
    public Map<String, Object> attributesToMap() {
        setDefaults();

        final QueryBuilderMap map = new QueryBuilderMap();

        map.put(OCCUR.fieldName, this.occur.typeName);
        map.putBooleanAsString(IS_GENERATED.fieldName, this.isGenerated);

        return map;
    }

    @Override
    public MatchAllQueryBuilder setAttributesFromMap(Map map) {
        BuilderUtils.castOccur(map.get(OCCUR.fieldName)).ifPresent(this::setOccur);
        BuilderUtils.castStringOrBooleanToBoolean(map.get(IS_GENERATED.fieldName)).ifPresent(this::setIsGenerated);
        return this;
    }

    public static MatchAllQueryBuilder matchall() {
        return new MatchAllQueryBuilder();
    }

}
