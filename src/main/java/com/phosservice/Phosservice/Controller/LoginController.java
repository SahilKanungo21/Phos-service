package com.phosservice.Phosservice.Controller;


import com.phosservice.Phosservice.Abstraction.ILoginService;
import com.phosservice.Phosservice.Abstraction.IUserService;
import com.phosservice.Phosservice.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/photos")
public class LoginController {

    @Autowired
    private  ILoginService iLoginService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private IUserService iUserService;


    @CrossOrigin("*")
    @PostMapping("/signIn/{userName}/{password}")
    public ResponseEntity<Object> login(@PathVariable("userName") String userName,
                                        @PathVariable("password") String password) {
        return new ResponseEntity<>(iLoginService.logIn(userName,password), HttpStatus.CREATED);
    }

}
