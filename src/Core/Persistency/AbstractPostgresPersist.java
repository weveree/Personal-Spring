package Core.Persistency;

import Core.Model.Column;
import Core.Model.IEntity;
import Core.Model.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class AbstractPostgresPersist<I,E extends IEntity<I>> implements IPersist<I,E>{
    Connection connection;
    Class<E> classEntity;

    public AbstractPostgresPersist(Connection connection,Class<E> classEntity) {
        this.connection = connection;
        this.classEntity=classEntity;
    }

    @Override
    public void add(E entity) {
        String query = String.format("insert into %",
                                        getTableName());
        try {
            Statement stmt = connection.prepareStatement(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTableName() {
        if(classEntity.isAnnotationPresent(Table.class))
        {
            return classEntity.getAnnotation(Table.class).value();
        }
        return classEntity.getSimpleName();
    }

    @Override
    public Stream<E> stream() {
        return Stream.empty();
    }

    @Override
    public Optional<List<E>> getAll() {
        try {
            PreparedStatement stmt = connection.prepareStatement("select * from "+getTableName());
            List<E> results = new ArrayList<>();
            ResultSet set =  stmt.executeQuery();
            while (set.next()){
                E entity = classEntity.getDeclaredConstructor().newInstance();
                for(Field field: classEntity.getDeclaredFields())
                {
                    if(field.isAnnotationPresent(Column.class))
                    {
                        field.setAccessible(true);
                        field.set(entity,set.getObject(field.getName()));
                        results.add(entity);
                    }
                }
                return Optional.of(results);
            }
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
