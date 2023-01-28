package com.phosservice.Phosservice.Services;

import com.phosservice.Phosservice.Abstraction.ILoginService;
import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.QueryComponent.CustomQueries;
import com.phosservice.Phosservice.Repository.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements ILoginService {
    Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private CustomQueries customQueries;

    public String logIn(String userName, String password) {
        if (userDao.existsById(userName) && customQueries.getPasswordForUser(userName).equals(password)) {
            LOGGER.info("Successfully Logged In {}", userName);
            /**
             * TODO : get the authentication User
             */
            return "Successfully Logged In";
        }
        throw new CustomException("Error while Login !! Try Again", HttpStatus.FORBIDDEN);
    }
}
