package oneachoice.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oneachoice.auth.dto.response.MessageDTO;
import oneachoice.auth.service.ReissueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<MessageDTO> reissue(HttpServletRequest request, HttpServletResponse response) {

        reissueService.reissue(request, response);

        return ResponseEntity.ok(new MessageDTO("Tokens reissued"));
    }
}
