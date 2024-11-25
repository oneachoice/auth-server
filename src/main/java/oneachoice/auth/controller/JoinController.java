package oneachoice.auth.controller;

import lombok.RequiredArgsConstructor;
import oneachoice.auth.dto.request.JoinDTO;
import oneachoice.auth.service.JoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<String> join(JoinDTO joinDTO) {
        if (joinService.join(joinDTO))
            return ResponseEntity.ok("Success");
        else
            return ResponseEntity.badRequest().body("Failure");
    }

}
