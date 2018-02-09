package hk.hku.cs.faceweb.controller;

import hk.hku.cs.faceweb.model.Face;
import hk.hku.cs.faceweb.model.Person;
import hk.hku.cs.faceweb.repository.PersonRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value = "/faceweb", produces = "application/json")
public class FaceController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(value = "/recognize", method = RequestMethod.POST, produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Recognize a human face and return the peron information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recognized"),
            @ApiResponse(code = 404, message = "No matched face found"),
            @ApiResponse(code = 500, message = "Internal server error")
    }
    )
    public ResponseEntity<JsonResponseMessage<Person>> Recognize(@RequestBody Face face) {
        logger.trace("Face: " + face);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonResponseMessage<Person>> response;
        try {
            JsonResponseMessage<Face> responseFace = restTemplate.postForObject("http://192.168.11.92:5002/recognize", face, FaceJsonResponseMessage.class);
            logger.debug("Person: " + responseFace);
            if (responseFace.getReturnCode() == 200) {
                Person person = personRepository.findByFace(responseFace.getContent());
                if (person != null) {
                    person.getFace().setScore(responseFace.getContent().getScore());
                    response = ResponseEntity.ok(new JsonResponseMessage<Person>(person, HttpStatus.OK.value(), "Recognized."));
                } else {
                    response = ResponseEntity.ok(new JsonResponseMessage<Person>(person, HttpStatus.NOT_FOUND.value(), "No matched face found."));
                }
            } else {
                response = ResponseEntity.status(responseFace.getReturnCode()).body(new JsonResponseMessage<Person>(null, responseFace.getReturnCode(), responseFace.getMessage()));
            }
        } catch (ResourceAccessException ex) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonResponseMessage<Person>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Fail to connect face engine."));
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonResponseMessage<Person>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
            logger.error(ex.getMessage(), ex);
        }
        return response;
    }
}
