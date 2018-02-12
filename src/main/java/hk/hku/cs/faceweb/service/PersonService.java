package hk.hku.cs.faceweb.service;

import hk.hku.cs.faceweb.exception.ErrorRemoveFaceException;
import hk.hku.cs.faceweb.exception.ErrorStoreFaceException;
import hk.hku.cs.faceweb.model.Person;
import java.util.List;

public interface PersonService {

    List<Person> findAll();
    Person findOne(Long id);
    Person save(Person person) throws ErrorStoreFaceException;
    void delete(Person person) throws ErrorRemoveFaceException;

}
