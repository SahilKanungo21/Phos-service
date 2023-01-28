package com.phosservice.Phosservice.Tables;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User", schema = "user-schema")
public class User implements Serializable {
    @Id
    String userName;
    String password;
    String name;
    Date createdDate;
    Date updatedDate;
}
