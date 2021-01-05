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
import querqy.model.builder.model.BuilderFieldProperties;
import querqy.model.builder.model.Occur;

import java.lang.reflect.Field;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldProperties.OCCUR;
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

    @BuilderField(fieldProperties = IS_GENERATED)
    private Boolean isGenerated;

    @BuilderField(fieldProperties = OCCUR)
    private Occur occur;

    public MatchAllQueryBuilder(final MatchAllQuery matchAllQuery) {
        this.setAttributesFromObject(matchAllQuery);
    }

    public MatchAllQueryBuilder(final Map map) {
        this.setAttributesFromWrappedMap(map);
    }

    @Override
    public Class<MatchAllQueryBuilder> getBuilderClass() {
        return MatchAllQueryBuilder.class;
    }

    @Override
    public MatchAllQueryBuilder getBuilder() {
        return this;
    }

    @Override
    public MatchAllQuery build(final DisjunctionMaxQuery parent) {
        setDefaults();
        return new MatchAllQuery(parent, Clause.Occur.SHOULD, isGenerated);
    }

    @Override
    public void setDefaults() {
//        final Field[] fields = MatchAllQueryBuilder.class.getDeclaredFields();
//
//        for (final Field field : fields) {
//            final BuilderField fieldAnnotation = field.getAnnotation(BuilderField.class);
//
//            if (nonNull(fieldAnnotation)) {
//                final BuilderFieldProperties properties = fieldAnnotation.fieldProperties();
//
//                try {
//                    final Object fieldValue = field.get(this);
//                    if (isNull(fieldValue)) {
//                        final Object defaulValue = properties.defaultValue;
//
//                        if (isNull(defaulValue) && fieldAnnotation.fieldIsMandatory()) {
//                            throw new QueryBuilderException(
//                                    String.format("Field %s is mandatory for builder %s",
//                                            field.getName(), getNameOfQueryType()));
//                        }
//
//                        field.set(this, defaulValue);
//                    }
//
//                } catch (IllegalAccessException e) {
//                    throw new QueryBuilderException(
//                            String.format("Error happened when setting defaults for field %s", field.getName()), e);
//                }
//            }
//        }
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
