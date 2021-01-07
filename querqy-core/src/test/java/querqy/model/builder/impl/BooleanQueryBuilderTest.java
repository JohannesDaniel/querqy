package querqy.model.builder.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import querqy.model.BooleanQuery;
import querqy.model.Clause;
import querqy.model.DisjunctionMaxQuery;
import querqy.model.Term;
import querqy.model.builder.AbstractBuilderTest;
import querqy.model.builder.QueryBuilderException;
import querqy.model.builder.QueryNodeBuilder;
import querqy.model.builder.model.BuilderField;
import querqy.model.builder.model.BuilderFieldProperties;
import querqy.model.builder.model.Occur;
import querqy.rewrite.commonrules.model.InputBoundary;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static querqy.model.builder.impl.BooleanQueryBuilder.bq;
import static querqy.model.builder.impl.DisjunctionMaxQueryBuilder.dmq;
import static querqy.model.builder.model.BuilderFieldProperties.CLAUSES;
import static querqy.model.builder.model.BuilderFieldProperties.IS_GENERATED;
import static querqy.model.builder.model.BuilderFieldProperties.OCCUR;

public class BooleanQueryBuilderTest extends AbstractBuilderTest {

    @Test
    public void test() throws JsonProcessingException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        Map<String, Object> map = map(
                entry(BooleanQueryBuilder.NAME_OF_QUERY_TYPE,
                        map(
                                entry(CLAUSES.fieldName,
                                        list(
                                                dmq("a").toMap(),
                                                dmq("b").toMap())),
                                entry(OCCUR.fieldName, Occur.MUST.typeName),
                                entry(IS_GENERATED.fieldName, "true"))));


        BooleanQueryBuilder bq = BooleanQueryBuilder.class.getDeclaredConstructor(Map.class).newInstance(map);
        BooleanQueryBuilder bq2 = BooleanQueryBuilder.class.getDeclaredConstructor(Map.class).newInstance(map);

        System.out.println(bq);
        Object dmq = dmq("a");

        System.out.println();
        System.out.println();
        System.out.println();

        Field field = BooleanQueryBuilder.class.getDeclaredField("clauses");
        Field field1 = BooleanQueryBuilder.class.getDeclaredField("occur");
        Field field2 = DisjunctionMaxQueryBuilder.class.getDeclaredField("clauses");

        field.setAccessible(true);
        field1.setAccessible(true);
        field2.setAccessible(true);

        System.out.println(field.getGenericType());
        System.out.println(field1.getGenericType());
        System.out.println(field2.getGenericType());

        System.out.println();

        System.out.println(field.getGenericType() instanceof ParameterizedType);
        System.out.println(field1.getGenericType() instanceof ParameterizedType);
        System.out.println(field2.getGenericType() instanceof ParameterizedType);

        System.out.println();
        System.out.println(field1.getGenericType().equals(Occur.class));
        System.out.println();

        Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        Type type2 = ((ParameterizedType) field2.getGenericType()).getActualTypeArguments()[0];

        Class<DisjunctionMaxQueryBuilder> type3 = DisjunctionMaxQueryBuilder.class;
        Class<?> type4 = BooleanQueryBuilder.class;
        // boolean bool2 = type3.equals((new DisjunctionMaxQueryBuilder(Collections.emptyMap())).getClass());
        boolean bool3 = (new BooleanQueryBuilder(map)).getClass().isInstance(BooleanQueryBuilder.class);
        boolean bool4 = (new BooleanQueryBuilder(map)).getClass().equals(type4);
        boolean bool5 = (new BooleanQueryBuilder(map)).getClass().isInstance(type4);
        type4.getDeclaredConstructor(Map.class).newInstance(map);

        Class<?> type1 = QueryNodeBuilder.class;

        boolean bool6 = type1.isInterface();
        boolean bool7 = type1.equals(QueryNodeBuilder.class);
        boolean bool8 = type1.isAssignableFrom(type1);


        System.out.println(Class.forName(type.getTypeName()));
        System.out.println(Class.forName(type2.getTypeName()));
        System.out.println(Class.forName(type.getTypeName()).isInterface());
        System.out.println(Class.forName(type2.getTypeName()).isInterface());


        field.set(bq, list(dmq));


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(bq);

        System.out.println(
                Class.forName(
                ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName())
                        .getDeclaredConstructor(List.class).newInstance(Collections.emptyList()));
    }

    @Test
    public void testSetAttributesFromMap() {
        assertThat(new BooleanQueryBuilder(
                map(
                        entry(BooleanQueryBuilder.NAME_OF_QUERY_TYPE,
                                map(
                                        entry(CLAUSES.fieldName,
                                                list(
                                                        dmq("a").toMap(),
                                                        dmq("b").toMap())),
                                        entry(OCCUR.fieldName, Occur.MUST.typeName),
                                        entry(IS_GENERATED.fieldName, "true")))

                ))).isEqualTo(
                        bq(list(dmq("a"), dmq("b")), Occur.MUST, true));
    }

    @Test
    public void testBuilderToMap() {
        BooleanQueryBuilder bqBuilder = bq(list(dmq("a"), dmq("b")), Occur.MUST, true);

        assertThat(bqBuilder.toMap())
                .isEqualTo(
                        map(
                                entry(BooleanQueryBuilder.NAME_OF_QUERY_TYPE,
                                        map(
                                                entry(CLAUSES.fieldName,
                                                        list(
                                                                dmq("a").toMap(),
                                                                dmq("b").toMap())),
                                                entry(OCCUR.fieldName, Occur.MUST.typeName),
                                                entry(IS_GENERATED.fieldName, "true")))));
    }

    @Test
    public void testSetAttributesFromObject() {
        BooleanQueryBuilder bqBuilder = bq(list(dmq("a"), dmq("b")), Occur.MUST, true);

        BooleanQuery bq = new BooleanQuery(null, Clause.Occur.MUST, true);

        DisjunctionMaxQuery dmq1 = new DisjunctionMaxQuery(bq, Clause.Occur.SHOULD, false);
        Term term1 = new Term(dmq1, "a");
        dmq1.addClause(term1);
        bq.addClause(dmq1);

        DisjunctionMaxQuery dmq2 = new DisjunctionMaxQuery(bq, Clause.Occur.SHOULD, false);
        Term term2 = new Term(dmq2, "b");
        dmq2.addClause(term2);
        bq.addClause(dmq2);

        assertThat(new BooleanQueryBuilder(bq)).isEqualTo(bqBuilder);
    }

    @Test
    public void testBuild() {
        BooleanQueryBuilder bqBuilder = bq(list(dmq("a"), dmq("b")), Occur.MUST, true);

        BooleanQuery bq = new BooleanQuery(null, Clause.Occur.MUST, true);

        DisjunctionMaxQuery dmq1 = new DisjunctionMaxQuery(bq, Clause.Occur.SHOULD, false);
        Term term1 = new Term(dmq1, "a");
        dmq1.addClause(term1);
        bq.addClause(dmq1);

        DisjunctionMaxQuery dmq2 = new DisjunctionMaxQuery(bq, Clause.Occur.SHOULD, false);
        Term term2 = new Term(dmq2, "b");
        dmq2.addClause(term2);
        bq.addClause(dmq2);

        assertThat(bqBuilder.build()).isEqualTo(bq);
    }

}
