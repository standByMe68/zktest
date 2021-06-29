package com.standbyme.zktest.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class InitWatcher implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("这里是初始化连接监听器");
    }
}
