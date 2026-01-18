package Core.Repository;

import Core.Model.IEntity;

import java.util.List;
import java.util.Optional;

public interface IRepository<I,E extends IEntity<I>> {
    void save(E entity);
    Optional<List<E>> findAll();
    Optional<E> findById(I id);
}
