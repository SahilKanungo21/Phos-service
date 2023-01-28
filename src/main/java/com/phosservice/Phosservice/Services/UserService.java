package com.phosservice.Phosservice.Services;

import com.phosservice.Phosservice.Abstraction.IUserService;
import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.QueryComponent.CustomQueries;
import com.phosservice.Phosservice.Repository.UserDao;
import com.phosservice.Phosservice.Tables.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService implements IUserService {
    Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private CustomQueries customQueries;

    public void createUser(String userMail, String password, String name) {
        User user = new User();
        user.setUserName(userMail);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedDate(new Date());
        user.setUpdatedDate(new Date());
        try {
            userDao.save(user);
            LOGGER.info("Successfully saved the user {}", user);
        } catch (CustomException ex) {
            LOGGER.error("Error while saving new user {} to Db", user);
            throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public String deleteUser(String userMail) {
        try {
            if (userDao.existsById(userMail)) {
                userDao.deleteById(userMail);
            }
        } catch (CustomException ex) {
            LOGGER.error("Error !! While deleting the user msg {} user{} ", ex.getMessage(), userMail);
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return "User Deleted Successfully !!!";
    }

    public String updateUser(User user) {

        if (userDao.existsById(user.getUserName())) {
            try {
                if (customQueries.updateUserDetails(user) > 0) {
                    return user + " updated Successfully";
                }
            } catch (CustomException ex) {
                throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        throw new CustomException(user + "does Not Exists in Db ", HttpStatus.BAD_REQUEST);
    }

    /**
     * TODO : develop a new api for reset User password
     */

}
