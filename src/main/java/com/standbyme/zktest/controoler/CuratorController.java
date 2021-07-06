package com.standbyme.zktest.controoler;


import com.standbyme.zktest.config.ZookeeperConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/curator")
public class CuratorController {

    @Autowired
    CuratorFramework curatorFramework;

    @Autowired
    ZookeeperConfig zookeeperConfig;


    @GetMapping("/createNode")
    public String createNode() {
        String s = null;
        try {
            s = curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/test11/test111", "hello curator".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @GetMapping("/modifyNode")
    public String modifyNode(String nodeText) {
        Stat stat = null;
        try {
            stat = curatorFramework.setData().withVersion(-1).forPath("/test11/test111", nodeText.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int version = stat.getVersion();
        return version + "";
    }

    @GetMapping("/deleteNode")
    public String deleteNode(Integer version) {
        Void unused = null;
        try {
            unused = curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().withVersion(version).forPath("/test11/test111");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ok";

    }

    @GetMapping("exitNode")
    public String exitNode() {
        Stat stat = null;
        try {
            stat = curatorFramework.checkExists().forPath("/test11/test11111");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stat == null ? "not this node" : stat.toString();
    }


    @GetMapping("selectNode")
    public String selectNode() {
        Stat stat = new Stat();
        byte[] bytes = new byte[0];
        try {
            bytes = curatorFramework.getData().storingStatIn(stat).forPath("/test11/test111");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(stat.getVersion());
        return new String(bytes);
    }

    @GetMapping("/chlidrenNode")
    public List<String> chlidrenNode() {
        List<String> strings = null;
        try {
            strings = curatorFramework.getChildren().forPath("/test11");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strings;
    }

    @GetMapping("/watcher")
    public String watcherNode() {

        NodeCache nodeCache = new NodeCache(curatorFramework, "/test11/test111");
        String nodeText = null;
        try {
            nodeCache.start(true);

            nodeText = nodeCache.getCurrentData()!= null?new String(nodeCache.getCurrentData().getData()):"null";

            nodeCache.getListenable().addListener(() ->{
                System.out.println("触发监听器");
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nodeText;
    }

    @GetMapping("/childrenWatcherNode")
    public String childrenWatcherNode() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, "/test11", true);

        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        pathChildrenCache.getListenable().addListener((curatorFramework,event) ->{
            System.out.println("event =="+ event);
        });
        return "ok";
    }

    @GetMapping("/lock")
    public String lock() {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(100000,50);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(zookeeperConfig.getHost(), 20000, 20000, retryPolicy);

        InterProcessLock lock = new InterProcessSemaphoreMutex(curatorFramework,"/calc/distributed-lock");

        try {
            lock.acquire(10, TimeUnit.SECONDS);
            Thread.sleep(20000);
            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "ok";
    }


}
