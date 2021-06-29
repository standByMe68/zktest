package com.standbyme.zktest.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class SelectWatcher implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {

        System.out.println("触发查询事件");


    }
}
