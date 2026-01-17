package Core.Persistency;

import Core.Model.IEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface IPersist<I,E extends IEntity> {
    void add(E entity);

    Stream<E> stream();

    Optional<List<E>> getAll();
}
