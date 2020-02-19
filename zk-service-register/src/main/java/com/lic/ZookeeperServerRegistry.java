package com.lic;
import org.apache.zookeeper.*;

import java.io.IOException;

/***
 * 注册中心 对外提供注册服务的
 */
public class ZookeeperServerRegistry {
    private ZooKeeper zk;

    public static final String root="/lic";  //需要手动创建根节点
    private static final String  host="192.168.2.100:2181,192.168.2.101:2181,192.168.2.102:2181";

    public ZooKeeper getConnection(Watcher watch) throws IOException {
        zk=new ZooKeeper(host,500,watch);
        return zk;
    }
}
