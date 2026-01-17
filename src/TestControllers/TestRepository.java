package TestControllers;


import Core.Repository.AbstractRepository;
import Core.Persistency.IPersist;

public class TestRepository extends AbstractRepository<String, TestModel> {

    public TestRepository(IPersist persist) {
        super(persist);
    }
}
