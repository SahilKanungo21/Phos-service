package com.phosservice.Phosservice.Services;

import com.phosservice.Phosservice.Abstraction.ILoginService;
import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.QueryComponent.CustomQueries;
import com.phosservice.Phosservice.Repository.JwtTokenDao;
import com.phosservice.Phosservice.Repository.UserDao;
import com.phosservice.Phosservice.Security.JwtTokenProvider;
import com.phosservice.Phosservice.Tables.JwtToken;
import com.phosservice.Phosservice.Tables.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class LoginService implements ILoginService {
    Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private CustomQueries customQueries;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenDao jwtTokenDao;

    public String logIn(String userName, String password) {
        User user = userDao.existsById(userName) ? userDao.findById(userName).get() : null;
        if (user != null && user.getPassword().equals(password)) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
                LOGGER.info("Successfully Logged In {}", userName);
                /**
                 * TODO : get the authentication User
                 */
                String token = jwtTokenProvider.createJwtToken(userName, user.getName());
                LOGGER.info("Successfully log in , user = {} ", user);
                return token;
            } catch (AuthenticationException ex) {
                throw new CustomException("invalid username or password. ", HttpStatus.UNAUTHORIZED);
            }
        }
        throw new CustomException("Invalid User name or Password", HttpStatus.FORBIDDEN);
    }

    public String logOut(String token) {
        try {
            jwtTokenDao.delete(new JwtToken(token));
            LOGGER.info("Successfully Logged out . token {} ", token);
            return "Successfully logged out";
        } catch (CustomException ex) {
            throw new CustomException("Error while Logging Out ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
