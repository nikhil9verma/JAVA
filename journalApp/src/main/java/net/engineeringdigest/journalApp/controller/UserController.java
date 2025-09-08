package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.Repository.UserRepo;
import net.engineeringdigest.journalApp.WeatherResponse;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WeatherService weatherService;

//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getAll();
//    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.createUser(user); // Uses validation
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error creating user: " + e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String authenticatedUsername = authentication.getName();

            User updatedUser = userService.updateUser(authenticatedUsername, user);
            return ResponseEntity.ok("User updated successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error updating user: " + e.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String authenticatedUsername = authentication.getName();

            userRepo.deleteByUserName(user.getUserName());
            return ResponseEntity.ok("User deleted successfully");

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping
    public ResponseEntity<?> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    WeatherResponse Weather_Response=weatherService.getWeather("Chandigarh");
    String greeting ="";
    if(Weather_Response!=null){
        greeting=" ,Weather feels like"+Weather_Response.getCurrent().getFeelslike_c();
    }
        return new ResponseEntity<>("Hi "+authentication.getName()+greeting,HttpStatus.OK);

    }
}




