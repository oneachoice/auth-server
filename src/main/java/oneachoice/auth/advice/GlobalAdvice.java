package oneachoice.auth.advice;

import jakarta.persistence.PersistenceException;
import lombok.extern.log4j.Log4j2;
import oneachoice.auth.dto.response.MessageDTO;
import oneachoice.auth.exception.TokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<?> handleTokenException(TokenException ex) {
        log.info(ex.toString());

        MessageDTO messageDTO = new MessageDTO(ex.getMessage());

        return ResponseEntity.status(ex.getHttpStatus()).body(messageDTO);
    }
}
