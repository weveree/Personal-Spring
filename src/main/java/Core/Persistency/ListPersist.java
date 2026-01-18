package Core.Persistency;

import Core.Model.IEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ListPersist<I,E extends IEntity> implements IPersist<I,E>{
    List<E> models = new ArrayList<>();
    @Override
    public void add(E entity) {
        models.add(entity);
    }

    @Override
    public Stream<E> stream(){
        return models.stream();
    }

    @Override
    public Optional<List<E>> getAll() {
        return Optional.ofNullable(models);
    }
    @Override
    public Optional<E> getOne(I id) {
        return this.models.stream()
                .filter(obj -> obj.getId().equals(id))
                .findFirst();
    }
}
