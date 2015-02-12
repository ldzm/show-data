<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="icon" href="./bootstrap-3.3.2/images/favicon.ico">
<title>日志分析</title>
<link href="./bootstrap-3.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="./bootstrap-3.3.2/navbar.css" rel="stylesheet">
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
<script type="text/javascript"
	src="./bootstrap-3.3.2/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
	var option = null;

	var getLineOption = function() {

		$.post('averageResponseTimeAction',
				$("#responseTime").serializeArray(), function(data) {
					option = data["option"];

					if (null != option) {
						// 路径配置
						require.config({
							paths : {
								echarts : 'http://echarts.baidu.com/build/dist'
							}
						});
						// 使用
						require([ 'echarts', 'echarts/chart/line' // 使用line状图就加载line模块，按需加载
						], function(ec) {
							// 基于准备好的dom，初始化echarts图表
							var myChart = ec.init(document
									.getElementById('main'));
							// 为echarts对象加载数据 
							myChart.setOption(option);
						});
					} else {
						alert("执行计算平均响应时间的任务失败，读取数据失败。");
					}
				});
	}
	var initFormData = function() {

		$.post('dataAction',
						{
							actionName : "Average"
						},
						function(data) {
							var data = data["data"];
							document.getElementById("requestType").value = data.requestType;
							document.getElementById("success").value = data.success;
							document.getElementById("basedir").value = data.basedir;
							document.getElementById("hadoopcmd").value = data.hadoopcmd;
							document.getElementById("taskdir").value = data.taskdir;
							document.getElementById("namelist").value = data.namelist;
							document.getElementById("inputfiledir").value = data.inputfiledir;
							document.getElementById("outputfiledir").value = data.outputfiledir;
							document.getElementById("interval").value = data.interval;
						});
	}
	
	var saveRequestType = function() {
		var requestTypeKeyVar = document.getElementById("requestTypeKey").value.trim();
		var requestTypeValueVar = document.getElementById("requestTypeValue").value.trim();
		
		if(null==requestTypeKeyVar || "" == requestTypeKeyVar || null == requestTypeValueVar || "" == requestTypeValueVar) {
			return false;
		}

		$('#addRequestTyeModal').modal('hide') 
		document.getElementById("requestTypeKey").value = "";
		document.getElementById("requestTypeValue").value = "";
		$.post('dataAction',{requestTypeKey : requestTypeKeyVar, requestTypeValue : requestTypeValueVar}, function(data,status) {
			
            if(("success" == status) && (data.requestTypeKey != "false")) {
    			document.getElementById("requestType").options.add(new Option(requestTypeKeyVar, requestTypeValueVar));
            }
		});
	}

	window.onload = function() {
		//初始化表单数据
		initFormData();
	}

</script>
<style type="text/css">
label[for="interval"] {
	color: black;
}
label[for="success"] {
	color: black;
}
label[for="inputfiledir"] {
	color: black;
}
label[for="basedir"] {
	color: black;
}
label[for="taskdir"] {
	color: black;
}
label[for="outputfiledir"] {
	color: black;
}
label[for="hadoopcmd"] {
	color: black;
}
label[for="namelist"] {
	color: black;
}
</style>
</head>
<body>
	<div class="container">
		<!-- Static navbar -->
		<nav class="navbar navbar-default">
			<div class="container-fluid">
				<div class="navbar-header">
					<a class="navbar-brand" href="#">日志分析</a>
				</div>
				<div id="navbar" class="navbar-collapse collapse">
					<ul class="nav navbar-nav">
						<li class="active"><a href="home">响应时间/吞吐量分析</a></li>
						<li><a href="analysis">综合分析</a></li>
					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
			<!--/.container-fluid -->
		</nav>

		<!-- Main component for a primary marketing message or call to action -->
		<div class="jumbotron">
			<s:form id="responseTime" name="responseTime" method="post">
				<tr>
					<td>请选择请求类型</td>
					<td> <s:select id="requestType" name="requestType"
							list="requestTypeMap" listKey="key" listValue="value"
							theme="simple" />
						<button type="button" class="btn btn-default" data-toggle="modal"
							data-target="#addRequestTyeModal" >添加请求类型</button>
					</td>
				</tr>
				<s:select class="selectpicker" id="success" name="success" label="成功/失败"
					list="#{'true':'成功', 'false':'失败'}" listKey="key" listValue="value"
					value="true">
				</s:select>
				<s:textfield id="basedir" label="文件所在HDFS" name="basedir" size="60" />
				<s:textfield id="hadoopcmd" label="hadoop命令的位置" name="hadoopcmd"
					size="60" />
				<s:textfield id="taskdir" label="hadoop任务所在位置" name="taskdir"
					size="60" />
				<s:textarea id="namelist" label="文件头" name="namelist" cols="60"
					rows="2" />
				<s:textfield id="inputfiledir" label="输入文件相对HDFS路径"
					name="inputfiledir" size="60" />
				<s:textfield id="outputfiledir" label="输出文件相对HDFS路径"
					name="outputfiledir" size="60" />
				<s:textfield id="interval" label="时间间隔(s)" name="interval" size="60" />
				<s:checkbox id="exetask" name="exetask" fieldValue="true" label="执行hadoop任务"/>
				<tr>
					<td><input type="button" class="btn btn-default" value="提交"
						onClick="getLineOption();" /></td>
					<td><input type="button" class="btn btn-default"
						value="重置表单数据 " onClick="initFormData();" /></td>
				</tr>
			</s:form>
			<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
			<div id="main" style="height: 400px"></div>
			<!-- ECharts单文件引入 -->
		</div>
	</div>
	<!-- /container -->

	<div class="modal fade" id="addRequestTyeModal" tabindex="-1"
		role="dialog" aria-labelledby="addRequestTyeModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="addRequestTyeModalLabel">添加请求类型</h4>
				</div>
				<div class="modal-body">
					<form>
						<div class="form-group">
							<label for="requestTypeKey" class="control-label">请球类型（如：HTTP Request）:</label>
							<input type="text" class="form-control" id="requestTypeKey">
						</div>
						<div class="form-group">
							<label for="requestTypeValue" class="control-label">展示的数据（如：HTTP 请求）:</label>
							<input type="text" class="form-control" id="requestTypeValue">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-default" onClick="saveRequestType();">确定</button>
				</div>
			</div>
		</div>
	</div>


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="./bootstrap-3.3.2/dist/js/bootstrap.min.js"></script>
	<script src="./bootstrap-3.3.2/assets/js/docs.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script
		src="./bootstrap-3.3.2/assets/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
