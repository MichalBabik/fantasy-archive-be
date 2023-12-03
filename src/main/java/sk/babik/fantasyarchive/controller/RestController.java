package sk.babik.fantasyarchive.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.service.FantasyUserService;

import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@CrossOrigin
public class RestController {

    private final FantasyUserService userService;

    @PostMapping("/user/create")
    @ResponseStatus(HttpStatus.CREATED)
    public FantasyUser createFantasyUser(@RequestBody FantasyUser userDto) {
        return userService.createFantasyUser(userDto);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<FantasyUser> getFantasyUser(@PathVariable Long id){
        FantasyUser user = userService.getFantasyUser(id);
        if (user != null) {
            return  new ResponseEntity<>(userService.getFantasyUser(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/check/{email}")
    public ResponseEntity<String> isFantasyUserWithEmail(@PathVariable String email){
        boolean isEmailAvailable = userService.isEmailAvailable(email);
        if (isEmailAvailable) {
            return new ResponseEntity<>("Email is available", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Email is already in use", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/user/authentication/{email}/{password}")
    public ResponseEntity<FantasyUser> getFantasyUser(@PathVariable String email, @PathVariable String password){
        FantasyUser fantasyUser = userService.getFantasyUserByEmail(email);
        if (fantasyUser == null) {
            return  new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            if (fantasyUser.getPassword().equals(password)) {
                return  new ResponseEntity<>(fantasyUser, HttpStatus.FOUND);
            } else {
                return  new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @GetMapping("/allUsers")
    public List<FantasyUser> getAllFantasyUsers() {
        return userService.getAllFantasyUsers();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> tryDeleteFantasyUserByID(@PathVariable Long id) {
            boolean isDeleted = userService.deleteFantasyUserById(id);
        if (isDeleted) {
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } else
            return  new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/user/deleteAccount/{email}")
    public ResponseEntity<String> tryDeleteFantasyUserByEmail(@PathVariable String email, @RequestBody FantasyUser fantasyUser) {
        boolean isDeleted = userService.deleteFantasyUserByEmail(email, fantasyUser);
        if (isDeleted) {
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } else
            return  new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<FantasyUser> updateFantasyUserById(@PathVariable Long id, @RequestBody FantasyUser fantasyUser) {
        FantasyUser user = userService.updateFantasyUserById(id, fantasyUser);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/updateUsername/{email}")
    public ResponseEntity<FantasyUser> updateFantasyUserByEmail(@PathVariable String email, @RequestBody FantasyUser fantasyUser) {
        FantasyUser user = userService.updateFantasyUserByEmail(email, fantasyUser);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}

