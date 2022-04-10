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

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(value = "/search/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUserByEmail(@RequestParam(required = true) String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping(value = "/search/firstName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUsersByFirstName(@RequestParam(required = true) String firstName) {
        return ResponseEntity.ok(userService.findUsersByFirstName(firstName));
    }

    @GetMapping(value = "/search/lastName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUsersByLastName(@RequestParam(required = true) String lastName) {
        return ResponseEntity.ok(userService.findUsersByLastName(lastName));
    }

    @GetMapping(value = "/search/position", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUsersByPosition(@RequestParam(required = true) String position) {
        return ResponseEntity.ok(userService.findUsersByPosition(position));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto user) throws Exception {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PutMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setPassword(@RequestBody PasswordDto dto) {
        userService.setPassword(dto);
        return ResponseEntity.noContent().build();
    }
}
