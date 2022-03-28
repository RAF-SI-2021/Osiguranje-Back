package crudApp.controllers;

import crudApp.dto.PasswordDto;
import crudApp.dto.UserCreateDto;
import crudApp.dto.UserDto;
import crudApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/logged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(userService.current());
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers() {
        if (userService.collectPermissions().getPermissions().isAdmin()) {
            return ResponseEntity.ok(userService.findAll());
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(value = "/search/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUserByEmail(@RequestParam(required = true) String email) {
        if (userService.collectPermissions().getPermissions().isAdmin()) {
            return ResponseEntity.ok(userService.findUserByEmail(email));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(value = "/search/firstName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUserByFirstName(@RequestParam(required = true) String firstName) {
        if (userService.collectPermissions().getPermissions().isAdmin()) {
            return ResponseEntity.ok(userService.findUserByFirstName(firstName));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(value = "/search/lastName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUserByLastName(@RequestParam(required = true) String lastName) {
        if (userService.collectPermissions().getPermissions().isAdmin()) {
            return ResponseEntity.ok(userService.findUserByLastName(lastName));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(value = "/search/position", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUserByPosition(@RequestParam(required = true) String position) {
        if (userService.collectPermissions().getPermissions().isAdmin()) {
            return ResponseEntity.ok(userService.findUserByPosition(position));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto user) throws Exception {
        if (userService.collectPermissions().getPermissions().isAdmin()) {
            return ResponseEntity.ok(userService.createUser(user));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UserDto user) {
        if (userService.collectPermissions().getPermissions().isAdmin()) {
            return ResponseEntity.ok(userService.updateUser(user));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setPassword(@RequestBody PasswordDto dto) {
        userService.setPassword(dto);
        return ResponseEntity.noContent().build();
    }
}
