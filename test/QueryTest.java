import Core.Query.QueryBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

public class QueryTest {
    @Test
    void buildTest() throws Exception {
        String query = new QueryBuilder()
                .select()
                .from("person")
                .where("name","=","'Yoyo'")
                .and()
                .where("id","in",new QueryBuilder().select().from("person").where("id","=","2"))
                .build();
        System.out.println(query);
    }

    @Test
    void insertTest() throws Exception {
        String query = new QueryBuilder()
                .insert(List.of("name"))
                .from("person")
                .values(List.of("'bob'"))
                .build();
        System.out.println(query);
    }

}
