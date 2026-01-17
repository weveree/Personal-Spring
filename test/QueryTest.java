import Core.Query.QueryBuilder;
import org.junit.jupiter.api.Test;

public class QueryTest {
    @Test
    void buildTest() {
        String query = new QueryBuilder()
                .where(a-> (Integer)a>3)
                .build();
        System.out.println(query);
    }
}
