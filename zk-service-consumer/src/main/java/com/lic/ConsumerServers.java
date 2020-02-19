package com.lic;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.SecureRandom;
import java.util.List;

/***
 * dubbo的 consumer
 */
public class ConsumerServers implements Watcher {
    Logger logger = LoggerFactory.getLogger(ConsumerServers.class);
    ZookeeperServerRegistry bitZook = new ZookeeperServerRegistry();
    private ZooKeeper zook;
    private String clientName;

    public StatDto useServer(List<String> data, int dom, int i)
            throws Exception {
        if (data.size() == i) {
            throw new Exception("没有可用的服务");
        }
        String node = data.get(dom);
        byte[] bytes = zook.getData(ZookeeperServerRegistry.root + "/" + node,
                true, null);
        String datas = new String(bytes);
        StatDto dto = JSON.parseObject(datas, StatDto.class);
        if (ServerStatus.stop.equals(dto.getStatus())) {
            i++;
            useServer(data, dom, i);
        }
        dto.setNode(node);
        return dto;

    }

    /**
     * 客户端订阅
     * @param clientName
     * @throws Exception
     */
    public void subscribe(String clientName) throws Exception {
        this.clientName = clientName;
        zook = bitZook.getConnection(this);
        List<String> data = zook.getChildren(ZookeeperServerRegistry.root, true); //获取根节点下的所有服务节点
        if (data.isEmpty()) {
            throw new Exception("没有可用的服务");
        }
        //随机选取一个服务
        int dom = new SecureRandom().nextInt(data.size());
        StatDto dto = useServer(data, dom, 0);
        dto.setNum(dto.getNum() + 1);
        dto.setName(dto.getName());
        dto.setClient(dto.getClient() == null ? clientName : dto.getClient() + "," + clientName);
        dto.setStatus(ServerStatus.run);
        //更新节点内容
        zook.setData(ZookeeperServerRegistry.root + "/" + dto.getNode(), JSON.toJSONString(dto).getBytes(), -1);

    }

    public void call() throws InterruptedException {
        System.out.println("客户端开启,建立netty连接");
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            if (event.getType() == EventType.NodeChildrenChanged) {
                System.out.println("服务器发生改变,重新订阅");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        ConsumerServers client = new ConsumerServers();
        client.subscribe(args[0]);
        try {
            client.call();
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
