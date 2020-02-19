<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<html>


<body>
		<h1><center>Zk服务注册中心后台管理</center></h1>
			<table class="table table-striped">
			<tr style="font-weight:bold">
				<td>服务名</td>
				<td>ip</td>
				<td>端口</td>
				<td>连接数</td>
				<td>负载均衡(轮询or随机)</td>
				<td>节点</td>
				<td>客服端</td>
				<td>状态(wait:无消费者,run:运行中,stop:禁用中)</td>
				<td>操作</td>
			</tr>
			<c:forEach items="${serverList}" var="server">
			<tr>
				<td class="warning">${server.name}</td>
				<td  class="success">${server.ip}</td>
				<td class="danger">${server.port}</td>
				<td class="warning">${server.num}</td>
				<td class="success">随机</td>
				<td class="danger">${server.node}</td>
				<td class="success">${server.client}</td>
				<td class="warning">${server.status}</td>
				<td class="success">
				<c:if test="${server.status=='stop' }">
					<a href="/admin/run?server=${server.node}">启动</a>
				</c:if>
				<c:if test="${server.status=='wait'}">
					<a href="#">休闲中</a>
				</c:if>
				<c:if test="${server.status=='run'}">
					<a href="/admin/stop?server=${server.node}">停用</a>
				</c:if>
				<c:if test="${server.status!='wait'}">
						<a href="/admin/delete?server=${server.node}">删除</a>
				</c:if>
			
				</td>
			</tr>
			
			</c:forEach>
			</table>
		
</body>


</html>
