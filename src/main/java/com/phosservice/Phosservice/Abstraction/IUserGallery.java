package com.phosservice.Phosservice.Abstraction;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public interface IUserGallery {

    void saveGalleryPhotos(String userName) throws InterruptedException, KeeperException, IOException;
}
