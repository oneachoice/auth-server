package oneachoice.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}
