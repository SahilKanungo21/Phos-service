package com.phosservice.Phosservice.Services;

import com.phosservice.Phosservice.Abstraction.IUserService;
import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.Exceptions.ValidationException;
import com.phosservice.Phosservice.QueryComponent.CustomQueries;
import com.phosservice.Phosservice.Repository.JwtTokenDao;
import com.phosservice.Phosservice.Repository.UserDao;
import com.phosservice.Phosservice.Security.JwtTokenProvider;
import com.phosservice.Phosservice.Security.pojo.SqlUserDetails;
import com.phosservice.Phosservice.Tables.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class UserService implements IUserService, UserDetailsService {
    Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private CustomQueries customQueries;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenDao jwtTokenDao;

    @PostConstruct
    public void init() {
        User user = new User();
        if (userDao.findAll().isEmpty()) {
            user.setUserName("kanungosahil123@gmail.com");
            user.setPassword(passwordEncoder.encode("sahil@766001"));
            user.setName("Sahil Kanungo");
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

    }

    public void createUser(String userMail, String name) {
        User user = new User();
        user.setUserName(userMail);
        user.setPassword(passwordEncoder.encode("password"));
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

    public String resetPassword(String updatedPassword) {
        String me = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            if (customQueries.updateUserPassword(me, updatedPassword) > 0) {
                return "Password reset successfully  !!!";
            }
        } catch (CustomException ex) {
            throw new CustomException("Error while reseting the password {} " + me, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new ValidationException("Failed !! Try Again", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.existsById(username) ? userDao.findById(username).get() : null;

        if (user == null) {
            throw new CustomException("Invalid user name or password", HttpStatus.UNAUTHORIZED);
        }

        SqlUserDetails sqlUserDetails = new SqlUserDetails(user.getUserName(), user.getPassword(), user.getName()
                , true, false, false, true,
                AuthorityUtils.createAuthorityList(user.getUserName()));
        LOGGER.info("Successfully loaded user by username {} ", sqlUserDetails);
        return sqlUserDetails;
    }
}


