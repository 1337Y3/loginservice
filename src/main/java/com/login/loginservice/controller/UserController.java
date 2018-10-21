package com.login.loginservice.controller;

import com.login.loginservice.model.User;
import com.login.loginservice.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
@RequestMapping("/login_api")
public class UserController {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    @RequestMapping(method = POST, value = "/adduser")
    public ResponseEntity addUser(@RequestBody User user) {
        User checkUser = userRepository.findByUsername(user.getUsername());
        if (checkUser != null) {
            LOGGER.error("User " + user.getUsername() + " already exists");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        userRepository.save(user);
        LOGGER.info("User " + user.getUsername() + " was added");
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/login", method = POST)
    public ResponseEntity loginUser(@RequestBody User user) {
        User checkUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(checkUser != null){
            if(!checkUser.getIsBlocked()) {
                LOGGER.info("User " + user.getUsername() + " is logged in");
                return new ResponseEntity(HttpStatus.OK);
            }
            LOGGER.error("User " + user.getUsername() + " is blocked");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        LOGGER.error("User " + user.getUsername() + " not found");
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/change/username/{username}", method = PUT)
    public ResponseEntity changeUserName(@RequestBody User user, @PathVariable String username) {
        User changedUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(changedUser != null) {
            if(!changedUser.getIsBlocked()) {
                changedUser.setUsername(username);
                userRepository.save(changedUser);
                LOGGER.info("User`s " + user.getUsername() + " username was changed to " + username);
                return new ResponseEntity(HttpStatus.OK);
            }
            LOGGER.error("User " + user.getUsername() + " is blocked");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        LOGGER.error("User " + user.getUsername() + " not found");
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/change/password/{password}", method = PUT)
    public ResponseEntity changeUserPassword(@RequestBody User user, @PathVariable String password) {
        User changedUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(changedUser != null) {
            if(!changedUser.getIsBlocked()) {
                changedUser.setPassword(password);
                userRepository.save(changedUser);
                LOGGER.info("User`s " + user.getPassword() + " password was changed to " + password);
                return new ResponseEntity(HttpStatus.OK);
            }
            LOGGER.error("User " + user.getUsername() + " is blocked");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        LOGGER.error("User " + user.getUsername() + " not found");
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/block/{username}", method = PUT)
    public ResponseEntity blockUser(@PathVariable String username) {
        User blockedUser = userRepository.findByUsername(username);
        if (blockedUser != null) {
            if (!blockedUser.getIsBlocked()) {
                blockedUser.setIsBlocked(true);
                userRepository.save(blockedUser);
                LOGGER.info("User " + username + " was successfully blocked");
                return new ResponseEntity(HttpStatus.OK);
            }
            LOGGER.error("User " + username + " is already blocked");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        LOGGER.error("User " + username + " not found");
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
