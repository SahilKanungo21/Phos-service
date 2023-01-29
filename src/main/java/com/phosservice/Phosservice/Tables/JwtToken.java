package com.phosservice.Phosservice.Tables;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JwtToken", schema = "jwt-schema")
public class JwtToken implements Serializable {
    @Id
    private String token;
}
