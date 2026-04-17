package TestControllers;

import Core.Connector.ConnectorFactory;
import Core.Persistency.PostgresPersist;

import java.util.List;

public class HomeService {
    TestRepository repo;
    public HomeService() {
        this.repo = new TestRepository(new PostgresPersist(ConnectorFactory.connection, TestModel.class));
    }

    public List<TestModel> getAll() {
        return repo.findAll().get();
    }

    public TestModel getOne(String name) {
        return repo.findById(name).orElseThrow();
    }
}
