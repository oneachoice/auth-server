package oneachoice.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oneachoice.auth.dto.request.JoinDTO;
import oneachoice.auth.dto.response.MessageDTO;
import oneachoice.auth.service.JoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<MessageDTO> join(@ModelAttribute @Valid JoinDTO joinDTO) {

        if (joinService.join(joinDTO))
            return ResponseEntity.ok(new MessageDTO("Joining success"));
        else
            return ResponseEntity.badRequest().body(new MessageDTO("Joining failure"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDTO>  handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        log.info("유효하지 않은 회원가입 요청 발생: {}", ex.toString());

        MessageDTO messageDTO = new MessageDTO("유효하지 않은 회원가입 요청입니다.");

        return ResponseEntity.badRequest().body(messageDTO);
    }
}
