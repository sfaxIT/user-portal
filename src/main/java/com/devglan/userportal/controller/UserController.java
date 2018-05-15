package com.devglan.userportal.controller;

import com.devglan.userportal.entity.User;
import com.devglan.userportal.service.UserService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller allows cross origin requests from "http://localhost:4200"
 */

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(UserController.BASE_URI)
@RequiredArgsConstructor
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final String BASE_URI = "/user-portal";

    private final UserService userService;

    private String userRole;

    @PostMapping
    public User createUser(@RequestBody User user) {
        LOGGER.info("Create user: "+user.getEMail());
        return userService.createUser(user);
    }

    @DeleteMapping("/{userId}")
    public User deleteUser(@PathVariable("userId") int userId) {
        return userService.deleteUser(userId);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

//    @GetMapping("/{userId}")
//    public User findUser(@PathVariable("userId") int userId) {
//        return userService.findUser(userId);
//    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{eMail}")
    public User findUser(@PathVariable("eMail") String eMail) {
        LOGGER.info("Value of user eMail: "+eMail);

        User user = userService.findUserByEMail(eMail);

        LOGGER.info("ID of user: "+user.getUserId());
        return user;
    }

    @GetMapping("/{username}/{password}")
//    @ResponseBody
    public User findLoggedUser(@PathVariable("username") String username,
                                     @PathVariable("password") String password) {
        LOGGER.info("Received login credentials: "+username+" & "+password);

        if((username != null) && (password != null)) {
            User user = userService.findUserByUsername(username);

            if(user != null) {
                String pwd = user.getPassword();
                if(pwd.equals(password)) {
                    LOGGER.info("User is logged with valid credentials: "+"username= "+username+" password= "+password);

                    LOGGER.info("STATUS CODE OF LOGGING: "+new ResponseEntity<>(HttpStatus.GONE).getStatusCode());
//                    return new ResponseEntity<>(HttpStatus.GONE).getStatusCode();
                    return user;
                } else {
                    LOGGER.info("Given password is wrong: "+password);

                    LOGGER.info("STATUS CODE OF LOGGING: "+new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode());
//                    return new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode();
                    return null;
                }
            } else {
                LOGGER.info("Given username is wrong: "+username);

                LOGGER.info("STATUS CODE OF LOGGING: "+new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode());
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode();
                return null;
            }
        } else {
            LOGGER.info("STAUS CODE OF LOGGING: "+new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode();
            return null;
        }
    }
}
