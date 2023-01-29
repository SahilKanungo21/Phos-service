package com.phosservice.Phosservice.Tables;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "UserGallery", schema = "gallery-schema")
public class WareHouse implements Serializable {
    @Id
    Long photoId;
    @Lob
    byte[] photoImage;
    String userName;
    Date createdPic;
}
