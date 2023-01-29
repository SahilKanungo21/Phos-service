package com.phosservice.Phosservice.Controller;

import com.phosservice.Phosservice.Abstraction.IUserService;
import com.phosservice.Phosservice.Controller.RequestController.UserRequest;
import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.Tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/photos")
public class UserController {


    @Autowired
    private IUserService iUserService;

    @CrossOrigin("*")
    @PostMapping("/save-user")
    public ResponseEntity<Object> saveUser(@RequestBody UserRequest userRequest) {
        try {
            iUserService.createUser(userRequest.getUserName(), userRequest.getFullName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin("*")
    @DeleteMapping("/delete-user/{emailId}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userName) {
        try {
            return new ResponseEntity<>(iUserService.deleteUser(userName), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin("*")
    @PostMapping("/update-user")
    public ResponseEntity<Object> saveUser(@RequestBody User userRequest) {
        try {
            return new ResponseEntity<>(iUserService.updateUser(userRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin("*")
    @PutMapping("/reset-password/{password}")
    public ResponseEntity<Object> resetPassword(@PathVariable String password) {
        try {
            return new ResponseEntity<>(iUserService.resetPassword(password), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
