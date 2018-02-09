package hk.hku.cs.faceweb.controller;


import hk.hku.cs.faceweb.model.Person;
import hk.hku.cs.faceweb.service.ErrorStoreFaceException;
import hk.hku.cs.faceweb.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.List;

@RestController
@RequestMapping(value = "/faceweb", produces = "application/json")
public class PeopleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    PersonService personService;

    @RequestMapping(value = "/people", method = RequestMethod.GET)
    public ResponseEntity<JsonResponseMessage<List<Person>>> personList() {
        ResponseEntity<JsonResponseMessage<List<Person>>> response;
        try {
            response = ResponseEntity.ok(new JsonResponseMessage<List<Person>>(personService.findAll(), HttpStatus.OK.value(), "List user success"));
        } catch (Exception ex) {
        response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonResponseMessage<List<Person>>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
        return response;
    }

    @RequestMapping(value = "/people/{id}", method = RequestMethod.GET)
    public Person person(@PathVariable Long id) {
        return personService.findOne(id);
    }


    @RequestMapping(value = "/people", method = RequestMethod.POST)
    public ResponseEntity<JsonResponseMessage<Person>> createPerson(@RequestBody Person person) {
        logger.debug("Person: " + person);
        ResponseEntity<JsonResponseMessage<Person>> response;
        try {
            person = personService.save(person);
            response = ResponseEntity.ok(new JsonResponseMessage<Person>(person, HttpStatus.OK.value(), "User create success" ));
        } catch (ResourceAccessException ex) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonResponseMessage<Person>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Fail to connect face engine."));
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonResponseMessage<Person>(person, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
            logger.error(ex.getMessage(), ex);
        }
        return response;
    }
}
