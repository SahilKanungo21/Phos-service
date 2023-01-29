package com.phosservice.Phosservice.QueryComponent;

import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.Tables.User;
import com.phosservice.Phosservice.Tables.WareHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CustomQueries {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getPasswordForUser(String userName) {
        String sql = "select user.password from User user where user.userName=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userName}, String.class);
    }

    public long updateUserDetails(User userobj) {
        Date updatedDate = new Date();
        String sql = "update User user set user.password=?," +
                "user.name=?, user.updatedDate =? where user.userName=?;";
        try {
            return jdbcTemplate.update(sql, new Object[]{userobj.getPassword(), userobj.getName(),
                    updatedDate, userobj.getUserName()});
        } catch (CustomException ex) {
            throw new CustomException("Error while updating the user {} " + userobj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public long updateUserPassword(String userName, String password) {
        Date updatedDate = new Date();
        String resetPassword = "update User user set user.password = ? where user.userName = ? ;";
        try {
            return jdbcTemplate.update(resetPassword, new Object[]{password, userName});
        } catch (CustomException ex) {
            throw new CustomException("Error while resetting the password for user " + userName,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public WareHouse checkIfImageExists(byte[] image) {
        String sql = "select * from user_gallery gallery where gallery.photoImage=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{image}, WareHouse.class);
    }
}
