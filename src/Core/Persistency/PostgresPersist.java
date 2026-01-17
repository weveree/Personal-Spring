package Core.Persistency;

import Core.Model.Column;
import Core.Model.IEntity;
import Core.Model.Table;
import Core.Query.QueryBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PostgresPersist<I, E extends IEntity<I>> implements IPersist<I, E> {
    Connection connection;
    Class<E> classEntity;

    public PostgresPersist(Connection connection, Class<E> classEntity) {
        this.connection = connection;
        this.classEntity = classEntity;
    }

    @Override
    public void add(E entity) {
        String query = null;
        try {
            query = new QueryBuilder()
                    .insert(getColumns(entity).stream().map(Field::getName).toList())
                    .from(getTableName())
                    .values(getColumnsValues(entity))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTableName() {
        if (classEntity.isAnnotationPresent(Table.class)) {
            return classEntity.getAnnotation(Table.class).value();
        }
        return classEntity.getSimpleName();
    }

    @Override
    public Stream<E> stream() {
        return getAll().map(List::stream).orElseGet(Stream::empty);
    }

    @Override
    public Optional<List<E>> getAll() {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    new QueryBuilder().select().from(getTableName()).build()
            );
            List<E> results = new ArrayList<>();
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                E entity = classEntity.getDeclaredConstructor().newInstance();
                for (Field field : getColumns(entity)) {
                    field.setAccessible(true);
                    field.set(entity, set.getObject(field.getName()));
                }
                results.add(entity);
            }
            return Optional.of(results);
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Field> getColumns(E entity) {
        List<Field> fields = new ArrayList<>();
        for (Field field : classEntity.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    @Override
    public Optional<E> getOne(I id) {
        E entity;
        try {
            entity = classEntity.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(new QueryBuilder().select().from(getTableName()).where(getColumns(entity).get(0).getName(),"=","'"+(String)id+"'").build());
            ResultSet set =  stmt.executeQuery();
            List<E> results = new ArrayList<>();

            while (set.next()) {
                E res = classEntity.getDeclaredConstructor().newInstance();

                for (Field field : getColumns(res)) {
                    field.setAccessible(true);
                    field.set(res, set.getObject(field.getName()));
                }
                results.add(res);
            }
            return Optional.ofNullable(results.get(0));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getColumnsValues(E entity) {
        List<String> values = new ArrayList<>();
        for (Field field : classEntity.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                try {
                    if (field.getType() == String.class)
                        values.add("'"+field.get(entity)+"'");
                    else
                        values.add((String) field.get(entity));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return values;
    }
}
