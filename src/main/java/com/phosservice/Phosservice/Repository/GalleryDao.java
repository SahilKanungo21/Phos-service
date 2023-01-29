package com.phosservice.Phosservice.Repository;

import com.phosservice.Phosservice.Tables.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryDao extends JpaRepository<WareHouse,String> {
}
