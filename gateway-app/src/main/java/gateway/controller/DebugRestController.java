package gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/debug")
public class DebugRestController {

    @Autowired
    private Environment env;

    @GetMapping(value = "/env", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> getProperty(@RequestParam(required = true) String property) {
        String propertyValue = env.getProperty(property);

        return ResponseEntity.ok(Objects.requireNonNullElseGet(propertyValue, () -> "Property " + property + "is null"));
    }
}
