package com.standbyme.zktest.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZookeeperConfig {

    @Value("${application.zookeeper.host}")
    private String host;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
