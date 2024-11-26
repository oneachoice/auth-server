package oneachoice.auth.advice;

import lombok.extern.log4j.Log4j2;
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
}
