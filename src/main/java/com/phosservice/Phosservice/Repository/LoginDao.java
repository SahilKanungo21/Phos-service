package com.phosservice.Phosservice.Repository;

import com.phosservice.Phosservice.Tables.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDao extends JpaRepository<Login, String> {
}
