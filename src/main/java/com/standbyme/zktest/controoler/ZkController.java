package com.standbyme.zktest.controoler;

import com.standbyme.zktest.config.SelectWatcher;
import com.standbyme.zktest.config.ZookeeperConfig;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/zk")
public class ZkController {

    @Autowired
    private ZookeeperConfig zookeeperConfig;

    @Autowired
    private ZooKeeper zooKeeper;

    @GetMapping("/host")
    public String test() {
        return zookeeperConfig.getHost();
    }


    @GetMapping("/createNode")
    public String setNode(String nodeName,String nodeText) {

        try {
            String s = zooKeeper.create("/" + nodeName, nodeText.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            return s;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/nodeList")
    public String nodeList(String nodePath) throws KeeperException, InterruptedException {

        List<String> children = zooKeeper.getChildren("/", null);

        return children.toString();
    }

    @GetMapping("/selectNode")
    public String selectNode(String path) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(path, new SelectWatcher(), null);
        return new String(data);
    }

    @GetMapping("/modify")
    public String modifyNode(String nodePath, String nodeText) throws KeeperException, InterruptedException {

        Stat stat = zooKeeper.setData(nodePath, nodeText.getBytes(StandardCharsets.UTF_8), -1);
        System.out.println(stat);


        return "stat";
    }






}
