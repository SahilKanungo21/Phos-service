package com.phosservice.Phosservice.Controller;

import com.phosservice.Phosservice.Abstraction.ILoginService;
import com.phosservice.Phosservice.Controller.RequestController.LoginRequest;
import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/photos")
public class LoginController {

    @Autowired
    private ILoginService iLoginService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @CrossOrigin("*")
    @PostMapping("/signIn")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(iLoginService.logIn(loginRequest.getUserName(), loginRequest.getPassword())
                , HttpStatus.CREATED);
    }

    @CrossOrigin
    @DeleteMapping("/signOut/{token}")
    public ResponseEntity<Object> logOut(@PathVariable("token") String token) {
        try {
            return new ResponseEntity<>(iLoginService.logOut(token), HttpStatus.OK);
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping("/requestToken/{token}")
    public ResponseEntity<Object> requestToken(String token) {
        try {
            return new ResponseEntity<>(jwtTokenProvider.createNewTokenFromExistingToken(token), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
