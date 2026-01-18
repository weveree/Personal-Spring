package TestControllers;

import Core.Model.AbstractEntity;
import Core.Model.Column;
import Core.Model.Table;

@Table("person")
public class TestModel extends AbstractEntity<String> {
    @Column
    String name;

    public TestModel(){

    }
    public TestModel(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getId() {
        return this.name;
    }

}
