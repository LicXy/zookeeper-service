package com.lic;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

	ZookeeperServerRegistry bitZook = new ZookeeperServerRegistry();
	private ZooKeeper zook;

	@RequestMapping("/list")
	public String getList(HttpServletRequest request) throws Exception {
		zook = bitZook.getConnection(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
			}
		});
		List<String> data = zook.getChildren(ZookeeperServerRegistry.root, true);
		List<StatDto> serverList = new ArrayList<StatDto>();
		for (String server : data) {
			byte[] bytes = zook.getData(ZookeeperServerRegistry.root + "/" + server,
					false, null);
			String datas = new String(bytes);
			StatDto dto = JSON.parseObject(datas, StatDto.class);
			serverList.add(dto);
		}
		request.setAttribute("serverList", serverList);
		return "admin";

	}

	@RequestMapping("/stop")
	public String stop(HttpServletRequest request) throws Exception {
		zook = bitZook.getConnection(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
			}
		});
		String server = request.getParameter("server");
		byte[] bytes = zook.getData(ZookeeperServerRegistry.root + "/" + server,
				false, null);
		String datas = new String(bytes);
		StatDto dto = JSON.parseObject(datas, StatDto.class);
		dto.setStatus(ServerStatus.stop);
		zook.setData(ZookeeperServerRegistry.root + "/" + server,
				JSON.toJSONString(dto).getBytes(), -1);
		
		return "redirect:/admin/list";
	}
	
	@RequestMapping("/run")
	public String run(HttpServletRequest request) throws Exception {
		zook = bitZook.getConnection(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
			}
		});
		String server = request.getParameter("server");
		byte[] bytes = zook.getData(ZookeeperServerRegistry.root + "/" + server,
				false, null);
		String datas = new String(bytes);
		StatDto dto = JSON.parseObject(datas, StatDto.class);
		dto.setStatus(ServerStatus.run);
		zook.setData(ZookeeperServerRegistry.root + "/" + server,
				JSON.toJSONString(dto).getBytes(), -1);
		return "redirect:/admin/list";
	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request) throws Exception {
		zook = bitZook.getConnection(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
			}
		});
		String server = request.getParameter("server");
		byte[] bytes = zook.getData(ZookeeperServerRegistry.root + "/" + server,
				false, null);
		String datas = new String(bytes);
		StatDto dto = JSON.parseObject(datas, StatDto.class);
		dto.setStatus(ServerStatus.stop);
		zook.delete(ZookeeperServerRegistry.root + "/" + server, -1);
		return "redirect:/admin/list";
	}

}
