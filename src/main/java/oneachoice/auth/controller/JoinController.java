package oneachoice.auth.controller;

import lombok.RequiredArgsConstructor;
import oneachoice.auth.dto.request.JoinDTO;
import oneachoice.auth.dto.response.MessageDTO;
import oneachoice.auth.service.JoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<MessageDTO> join(JoinDTO joinDTO) {
        if (joinService.join(joinDTO))
            return ResponseEntity.ok(new MessageDTO("Joining success"));
        else
            return ResponseEntity.badRequest().body(new MessageDTO("Joining failure"));
    }

}
