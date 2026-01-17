import Core.Connector.Connector;
import Core.Model.Column;
import TestControllers.TestModel;
import Core.Persistency.AbstractPostgresPersist;
import Core.Persistency.ListPersist;
import Core.Repository.IRepository;
import TestControllers.TestRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryTest {
    @Test
    public void saveEntity(){
        IRepository<String,TestModel> repo = new TestRepository(new ListPersist());
        TestModel model = new TestModel("Hey");
        repo.save(model);

        System.out.println("end");
    }

    @Test
    void DBConnectionTest() throws SQLException {
        Connection pgsql = Connector.postgres("192.168.1.45",5431,"postgres","wawa","wawa");
        PreparedStatement stmt = pgsql.prepareStatement("select * from person");
        stmt.executeQuery();
        ResultSet res = stmt.getResultSet();

        while (res.next()){
            System.out.println(res.getString(1));
        }
    }
    @Test
    void PostgresInsertTest() throws SQLException {
        Connection pgsql = Connector.postgres("192.168.1.45",5431,"postgres","wawa","wawa");
        IRepository<String,TestModel> repo = new TestRepository(new AbstractPostgresPersist<>(pgsql, TestModel.class));
        System.out.println(repo.findAll().get().get(0).getName());
    }

    @Test
    void getFieldsEntity() {
        Class<?> _class = TestModel.class;
        List<Field> fields = new ArrayList<>();
        for(Field field: _class.getDeclaredFields())
        {
            if(field.isAnnotationPresent(Column.class))
            {
                fields.add(field);
            }
        }
        return ;
    }

}
