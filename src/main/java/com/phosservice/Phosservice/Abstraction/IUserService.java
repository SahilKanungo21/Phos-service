package com.phosservice.Phosservice.Abstraction;

import com.phosservice.Phosservice.Tables.User;

public interface IUserService {
    void createUser(String userMail, String password, String name);
    String deleteUser(String userMail);
    String updateUser(User user);
}