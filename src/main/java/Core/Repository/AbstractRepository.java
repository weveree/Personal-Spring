package Core.Repository;

import Core.Model.IEntity;
import Core.Persistency.IPersist;

import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<I,E extends IEntity<I>> implements IRepository<I,E>{
    IPersist<I, E> table;

    public AbstractRepository(IPersist persist) {
        this.table = persist;
    }

    @Override
    public void save(E entity) {
//        E toSave = findById(entity.getId()).or();
        table.add(entity);
    }


    public Optional<E> findById(I id){
        return table.getOne(id);
    }
    @Override
    public Optional<List<E>> findAll(){
        return table.getAll();
    }
}
