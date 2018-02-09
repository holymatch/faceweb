package hk.hku.cs.faceweb.service;

import hk.hku.cs.faceweb.model.Person;
import java.util.List;

public interface PersonService {

    public List<Person> findAll();
    public Person findOne(Long id);
    public Person save(Person person) throws  ErrorStoreFaceException;
}
