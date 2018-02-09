package hk.hku.cs.faceweb.service;

import hk.hku.cs.faceweb.controller.JsonResponseMessage;
import hk.hku.cs.faceweb.model.Person;
import hk.hku.cs.faceweb.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
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
    public Person save(Person person) throws  ErrorStoreFaceException{
        person = personRepository.save(person);
        RestTemplate restTemplate = new RestTemplate();
        JsonResponseMessage<Person> responsePerson = restTemplate.postForObject("http://192.168.11.92:5002/face", person.getFace(), JsonResponseMessage.class);
        if (responsePerson.getReturnCode() != 200) {
            throw new ErrorStoreFaceException(responsePerson.getMessage());
        }
        logger.debug("responsePerson: " + responsePerson);
        return person;
    }
}
