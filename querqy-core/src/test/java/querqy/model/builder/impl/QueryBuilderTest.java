package querqy.model.builder.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.util.Map;

import static querqy.model.builder.impl.BooleanQueryBuilder.bq;
import static querqy.model.builder.impl.DisjunctionMaxQueryBuilder.dmq;
import static querqy.model.builder.impl.TermBuilder.term;

public class QueryBuilderTest {

    @Test
    public void testSetAttributesFromMap() {
    }

    @Test
    public void testBuilderToMap() {
    }

    @Test
    public void testBuild() {
    }

    @Test
    public void testSetAttributesFromObject() {
    }


    @Test
    public void test3() {
//        Builder builder = new Builder();
//
//        builder.test();
    }


    @Test
    public void test2() throws JsonProcessingException {
        TermBuilder term = term("a");
        System.out.println(term);



        System.out.println(
                new ObjectMapper()
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .enable(SerializationFeature.WRAP_ROOT_VALUE)
                        .enable(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)
                        .writeValueAsString(term)
//                        .convertValue(term, Map.class)
        );

        System.out.println(
                new ObjectMapper()
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .enable(SerializationFeature.WRAP_ROOT_VALUE)
                        .convertValue(term, Map.class)
        );

        DisjunctionMaxQueryBuilder dmq = dmq("a", "b");
        System.out.println(dmq);

        System.out.println(
                new ObjectMapper()
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .enable(SerializationFeature.WRAP_ROOT_VALUE)
                        .convertValue(dmq, Map.class)
        );

        QueryBuilder query = QueryBuilder.query(dmq("a", "b"), dmq(term("c"), bq("d", "e")));
        System.out.println(query);

        System.out.println(
                new ObjectMapper()
                        .enable(SerializationFeature.WRAP_ROOT_VALUE)
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .convertValue(query, Map.class)
        );


    }

    @Test
    public void test() throws JsonProcessingException {

        ObjectWriter objectWriter = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                //.enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writerWithDefaultPrettyPrinter();

//        TermBuilder term = term("a");
//        System.out.println(term);
//        System.out.println(objectWriter.writeValueAsString(term));
//
//        DisjunctionMaxQueryBuilder dmq = dmq("a", "b");
//        System.out.println(dmq);
//        System.out.println(objectWriter.writeValueAsString(dmq));
//
        QueryBuilder query = QueryBuilder.query(dmq("a", "b"), dmq(term("c"), bq("d", "e")));
        System.out.println(query);
        System.out.println(objectWriter.writeValueAsString(query));



    }
}
