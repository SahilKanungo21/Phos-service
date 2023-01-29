package com.phosservice.Phosservice.Abstraction;

import com.phosservice.Phosservice.Tables.User;

public interface IUserService {
    void createUser(String userMail, String name);

    String deleteUser(String userMail);

    String updateUser(User user);

    String resetPassword(String updatedPassword);
}