package org.lpro.player.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequest extends RuntimeException {

    private static final long serialVersionUID = 12344567890L;

    public BadRequest(String message) {
        super(message);
    }

    public BadRequest(String message, Throwable cause) {
        super(message, cause);
    }

}