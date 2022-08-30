package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
        
    @GetMapping
    public List<User> findAllUsers(@RequestParam(value = "id", required = false, defaultValue = "0") int id) {
        List<User> users = (List<User>) userRepository.findAll();
        if(id == 1) {
            // odd
            users = users.stream().filter(p -> p.getId() % 2 == 1).collect(Collectors.toList());
        }
        else if(id == 2) {
            // even
            users = users.stream().filter(p -> p.getId() % 2 == 0).collect(Collectors.toList());
        }
        return users;
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable(value = "id") long id) {
       // Implement
       Optional<User> user = userRepository.findById(id);
 
        if(user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        } 
        return ResponseEntity.notFound().build();
    }
 
    @PostMapping
    public User saveUser(@Validated @RequestBody User user) {
        // Implement
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @RequestBody User _user) {

        Optional<User> userData = userRepository.findById(id);  // finds the user with the provided id and update it

        if(userData.isPresent()) {
            User user = userData.get();
            user.setName(_user.getName());
            return new ResponseEntity<>(userRepository.save(user).toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTutorial(@PathVariable("id") long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("User " + id + " Successfully deleted.", HttpStatus.OK);
    }
}
