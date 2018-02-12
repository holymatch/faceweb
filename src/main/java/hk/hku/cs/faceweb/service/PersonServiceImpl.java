package hk.hku.cs.faceweb.service;

import hk.hku.cs.faceweb.controller.response.JsonResponseMessage;
import hk.hku.cs.faceweb.exception.ErrorRemoveFaceException;
import hk.hku.cs.faceweb.exception.ErrorStoreFaceException;
import hk.hku.cs.faceweb.exception.PersonNotFoundException;
import hk.hku.cs.faceweb.model.Person;
import hk.hku.cs.faceweb.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Person findOne(Long id) {
        return personRepository.findOne(id);
    }

    @Override
    @Transactional
    public Person save(Person person) throws ErrorStoreFaceException {
        person = personRepository.save(person);
        RestTemplate restTemplate = new RestTemplate();
        JsonResponseMessage<Person> responsePerson = restTemplate.postForObject("http://192.168.11.92:5002/face", person.getFace(), JsonResponseMessage.class);
        if (responsePerson.getReturnCode() != 200) {
            throw new ErrorStoreFaceException(responsePerson.getMessage());
        }
        logger.debug("responsePerson: " + responsePerson);
        return person;
    }

    @Override
    @Transactional
    public Person update(Person person) throws ErrorStoreFaceException, PersonNotFoundException {
        Person dbPerson = personRepository.findOne(person.getId());
        if (dbPerson == null) {
            throw new PersonNotFoundException("Person with id " + person.getId() + " not found.");
        } else if(dbPerson.getFace().getFaceData().equals(person.getFace().getFaceData())){
            return personRepository.save(person);
        } else {
            return save(person);
        }
    }

    @Override
    @Transactional
    public void delete(Person person) throws ErrorRemoveFaceException {
        personRepository.delete(person);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete("http://192.168.11.92:5002/face/" + person.getFace().getIdentify());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new ErrorRemoveFaceException("Unable to remove face in face engine.");
        }
    }
}
