package com.standbyme.zktest.config;


import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ZKConfig {

    @Autowired
    ZookeeperConfig zookeeperConfig;

    @Bean
    public ZooKeeper getZookeeper() throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper(zookeeperConfig.getHost(), 20000, new InitWatcher());
        return zooKeeper;
    }
}
