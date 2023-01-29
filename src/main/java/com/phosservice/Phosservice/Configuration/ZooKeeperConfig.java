package com.phosservice.Phosservice.Configuration;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ZooKeeperConfig {
    @Value("${spring.zookeeper.hosts}")
    private String ZK_HOSTS;

    @Bean
    public ZooKeeper zooKeeper() throws IOException {
        int SESSION_TIMEOUT = 5000;
        return new ZooKeeper(ZK_HOSTS, SESSION_TIMEOUT, null);
    }
}
