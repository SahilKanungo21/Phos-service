package com.phosservice.Phosservice.Abstraction;

public interface ILoginService {
    String logIn(String userName, String password);

    String logOut(String token);
}
