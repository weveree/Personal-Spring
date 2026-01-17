package TestControllers;

import Core.Connector.Connector;
import Core.Persistency.PostgresPersist;

import java.util.List;

public class HomeService {
    TestRepository repo;
    public HomeService() {
        this.repo = new TestRepository(new PostgresPersist(Connector.connection, TestModel.class));
    }

    public List<TestModel> getAll() {
        return repo.findAll().get();
    }
}
