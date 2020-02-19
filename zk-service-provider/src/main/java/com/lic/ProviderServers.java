package com.lic;
import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.*;


import java.io.IOException;

public class ProviderServers implements Watcher {

    ZookeeperServerRegistry bitZookeeperServer=new ZookeeperServerRegistry();

    /***
     * 注册
     * @param serverName
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    void register(String serverName) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zook= bitZookeeperServer.getConnection(this);
        //创建服务节点
        String node=  zook.create(ZookeeperServerRegistry.root+"/server",serverName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("创建成功过"+serverName+"，节点："+node);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        ProviderServers server=new ProviderServers();
        StatDto stat=new StatDto();
        int i=0;
        stat.setIp(args[i++]);  //IP
        stat.setPort(args[i++]); //端口
        stat.setName(args[i++]);  //服务名称
        stat.setNum(0);
        stat.setStatus(ServerStatus.wait);  //服务状态
        server.register(JSON.toJSONString(stat));
        Thread.sleep(Long.MAX_VALUE);
    }


}
