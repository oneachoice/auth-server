package oneachoice.auth.advice;

import jakarta.persistence.PersistenceException;
import lombok.extern.log4j.Log4j2;
import oneachoice.auth.dto.response.MessageDTO;
import oneachoice.auth.exception.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Log4j2
@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) {
        log.error(ex.toString());
    }

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException ex) {
        log.error(ex.toString());
    }

    @ExceptionHandler(PersistenceException.class)
    public void handlePersistenceException(PersistenceException ex) {
        log.error(ex.toString());
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDTO handleTokenException(TokenException ex) {

        log.info(ex.toString());

        return new MessageDTO(ex.getMessage());
    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDTO handleBindException(BindException ex) {

        log.info(ex.toString());

        return new MessageDTO(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
    }
}
