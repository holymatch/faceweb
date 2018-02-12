package hk.hku.cs.faceweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Peron not found.")
public class PersonNotFoundException extends Exception {
    public PersonNotFoundException(String message) {
        super(message);
    }
}
