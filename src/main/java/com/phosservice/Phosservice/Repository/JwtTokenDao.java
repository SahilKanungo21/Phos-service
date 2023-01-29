package com.phosservice.Phosservice.Repository;

import com.phosservice.Phosservice.Tables.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenDao extends JpaRepository<JwtToken,String> {
}
