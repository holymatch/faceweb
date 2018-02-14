package hk.hku.cs.faceweb.controller;


import hk.hku.cs.faceweb.controller.response.JsonResponseMessage;
import hk.hku.cs.faceweb.exception.ErrorRemoveFaceException;
import hk.hku.cs.faceweb.exception.PersonNotFoundException;
import hk.hku.cs.faceweb.model.Person;
import hk.hku.cs.faceweb.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

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


    @RequestMapping(value = "/people/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletePerson(@PathVariable Long id) throws PersonNotFoundException {
        try {
            Person person = personService.findOne(id);
            if (person == null) {
                throw new PersonNotFoundException("Person with id " + id + " cannot be found");
            }
            personService.delete(person);
        } catch (ErrorRemoveFaceException ex) {
            logger.error(ex.getMessage(),ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/people", method = RequestMethod.POST)
    public ResponseEntity<JsonResponseMessage<Person>> createPerson(@RequestBody Person person) {
        logger.debug("Person: " + person);
        ResponseEntity<JsonResponseMessage<Person>> response;
        response = savePerson(person);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            response.getBody().setMessage("User create success");
        }
        return response;
    }

    @RequestMapping(value = "/people/{id}", method = RequestMethod.PUT)
    public ResponseEntity<JsonResponseMessage<Person>> updatePerson(@PathVariable Long id, @RequestBody Person person) {

        ResponseEntity<JsonResponseMessage<Person>> response;
        logger.debug("Person: " + person);
        if (id.equals(person.getId())) {
            Person dbPerson = personService.findOne(id);
            if (dbPerson == null ) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponseMessage<Person>(person, HttpStatus.NOT_FOUND.value(), "Person with id " + id + " not found in db." ));
            } else {
                response = savePerson(person);
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    response.getBody().setMessage("User update success");
                }
            }
        } else {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseMessage<Person>(person, HttpStatus.BAD_REQUEST.value(), "The person id is not match with person object."));
        }
        return response;
    }

    private ResponseEntity<JsonResponseMessage<Person>> savePerson(Person person) {
        ResponseEntity<JsonResponseMessage<Person>> response;
        try {
            person = personService.save(person);
            response = ResponseEntity.ok(new JsonResponseMessage<Person>(person, HttpStatus.OK.value(), null ));
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
