package com.phosservice.Phosservice.Tables;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JwtToken", schema = "jwt-schema")
public class JwtToken {
    @Id
    private String token;
}
