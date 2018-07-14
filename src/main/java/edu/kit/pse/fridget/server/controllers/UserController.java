package edu.kit.pse.fridget.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kit.pse.fridget.server.models.User;
import edu.kit.pse.fridget.server.models.representations.UserWithJwtRepresentation;
import edu.kit.pse.fridget.server.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserWithJwtRepresentation> registerOrLoginWithIdToken(@RequestParam("idToken") String googleIdToken) {
        return new ResponseEntity<>(service.registerOrLoginWithIdToken(googleIdToken), HttpStatus.CREATED);
    }
}