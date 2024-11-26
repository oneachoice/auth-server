package oneachoice.auth.advice;

import lombok.extern.log4j.Log4j2;
import oneachoice.auth.exception.TokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class TokenAdvice {

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handleTokenException(TokenException ex) {

        log.info(ex.toString());

        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }
}
