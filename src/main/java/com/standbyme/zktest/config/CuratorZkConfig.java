package com.standbyme.zktest.config;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorZkConfig {

    @Autowired
    ZookeeperConfig zookeeperConfig;

    @Bean
    public CuratorFramework getCuratorFramework() {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(100000,50);

        CuratorFramework build = CuratorFrameworkFactory.builder()
                .connectString(zookeeperConfig.getHost())
                .connectionTimeoutMs(3000)
                .sessionTimeoutMs(30000)
                .retryPolicy(retryPolicy)
                .namespace("")
                .build();
        build.start();

        return build;
    }


}
