package com.phosservice.Phosservice.Services;

import com.phosservice.Phosservice.Abstraction.IUserGallery;
import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.QueryComponent.CustomQueries;
import com.phosservice.Phosservice.Repository.GalleryDao;
import com.phosservice.Phosservice.Repository.UserDao;
import com.phosservice.Phosservice.Tables.User;
import com.phosservice.Phosservice.Tables.WareHouse;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserGalleryServices implements IUserGallery {
    Logger LOGGER = LoggerFactory.getLogger(UserGalleryServices.class);

    @Autowired
    private ZooKeeper zooKeeper;

    @Autowired
    private GalleryDao galleryDao;

    @Autowired
    private CustomQueries customQueries;

    @Autowired
    private UserDao userDao;

    private long fetchCounterFromZK() throws InterruptedException, KeeperException {
        if (zooKeeper.exists("/counter", false) == null) {
            zooKeeper.create("/counter", "0".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        return zooKeeper.setData("/counter", "".getBytes(), -1).getVersion();
    }

    public void saveGalleryPhotos(String userName)
            throws InterruptedException, KeeperException, IOException {
        /**
         *  TODO : get the userName from SecurityContextHolder
         */
        User user = userDao.findById(userName).orElse(null);

        if (user == null) {
            throw new CustomException(userName + " does not exists in UserDb", HttpStatus.BAD_REQUEST);
        }

        // get the photo ids
        long photoId = fetchCounterFromZK();
        WareHouse userPhotos = new WareHouse();
        userPhotos.setUserName(userName);
        // get the image from cloud server
        userPhotos.setPhotoImage(Files.readAllBytes(
                Paths.get("C:\\Users\\kanun\\OneDrive\\Desktop\\System Design")));
        userPhotos.setPhotoId(photoId);
        userPhotos.setCreatedPic(new Date());

        try {
            galleryDao.save(userPhotos);
            LOGGER.info("Successfully saved the photoId " + photoId + " by " + userName);
        } catch (CustomException ex) {
            LOGGER.error("Error while saving user photo to db {} ", userPhotos);
            throw new CustomException("Error while saving new User photos ",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * TODO : delete the image from db
     *  TODO : get the statistics for each user
     *  TODO : get the heartbeat for each user
     *  TODO : calculate the health status of table
     *  TODO : fetch the gallery group by userName with pagination
     *  TODO : fetch the gallery data
     */

    public String deletePhotos(String path) throws IOException {
        byte[] image = Files.readAllBytes(Paths.get(path));
        // write a custom query to check if it exists or not
        WareHouse wareHouse = customQueries.checkIfImageExists(image);
        if (wareHouse == null) {
            throw new CustomException(path + " does not exists in db", HttpStatus.BAD_REQUEST);
        }
        try {
            galleryDao.delete(wareHouse);
            LOGGER.info(wareHouse + "deleted successfully !!");
        } catch (CustomException ex) {
            LOGGER.error("deletion failed for pic {}", image);
            throw new CustomException("Error while deleting the photos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    public List<WareHouse> getAll() {
        try {
            return galleryDao.findAll();
        } catch (CustomException ex) {
            throw new CustomException("Error while fetching the data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
