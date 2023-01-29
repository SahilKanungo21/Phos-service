package com.phosservice.Phosservice.Tables;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User-Gallery", schema = "gallery-schema")
public class WareHouse {
    @Id
    String userName;
    Set<Integer> photoIds;
}
