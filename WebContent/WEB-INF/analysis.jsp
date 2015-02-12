<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="icon" href="./bootstrap-3.3.2images/favicon.ico">
<title>日志分析</title>
<link href="./bootstrap-3.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="./bootstrap-3.3.2/navbar.css" rel="stylesheet">
<script type="text/javascript">
	var altRows = function(id) {
		if (document.getElementsByTagName) {
			var table = document.getElementById(id);
			var rows = table.getElementsByTagName("tr");
			for (i = 0; i < rows.length; i++) {
				if (i % 2 == 0) {
					rows[i].className = "evenrowcolor";
				} else {
					rows[i].className = "oddrowcolor";
				}
			}
		}
	}


	var initFormData = function() {

		$
				.post(
						'dataAction',
						{
							actionName : "Analysis"
						},

						function(data) {
							var data = data["data"];
							document.getElementById("basedir").value = data.basedir;
							document.getElementById("hadoopcmd").value = data.hadoopcmd;
							document.getElementById("taskdir").value = data.taskdir;
							document.getElementById("namelist").value = data.namelist;
							document.getElementById("inputfiledir").value = data.inputfiledir;
							document.getElementById("outputfiledir").value = data.outputfiledir;
						});
	}

	window.onload = function() {
		altRows('alternatecolor');

		//初始化表单数据
		initFormData();
	}
</script>

<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<style type="text/css">
table.altrowstable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}

table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}

table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}

.oddrowcolor {
	background-color: #d4e3e5;
}

.evenrowcolor {
	background-color: #c3dde0;
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
						<li><a href="home">响应时间/吞吐量分析</a></li>
						<li class="active"><a href="analysis">综合分析</a></li>
					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
			<!--/.container-fluid -->
		</nav>
		<!-- Main component for a primary marketing message or call to action -->
		<div class="jumbotron">
			<s:form id="analysisSummery" action="analysisAction" method="post">
				<s:textfield id="basedir" label="文件所在HDFS" name="basedir" />
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
				<s:checkbox id="exetask" name="exetask" fieldValue="true" label="执行hadoop任务"/>
				<tr>
					<td><s:submit value="提交" class="btn btn-default" align="left"
							theme="simple" /></td>

					<td><input type="button" class="btn btn-default"
						value="重置化表单数据" onClick="initFormData();" /></td>
				</tr>
			</s:form>
			<table class="altrowstable" id="alternatecolor">
				<tr>
					<th>Label</th>
					<th>Start time</th>
					<th>End time</th>
					<th>Max</th>
					<th>Min</th>
					<th>Mean</th>
					<th>Succ Mean</th>
					<th>Std.Dev</th>
					<th>Succ Std.Dev</th>
					<th>Throughput(M)</th>
					<th>Fail Rate</th>
					<th>Succ Count</th>
					<th>Fail Count</th>
				</tr>
				<s:iterator value="%{analysisResultBeans}">
					<tr>
						<td><s:property value="label" /></td>
						<td><s:date name="startTime" format="yyyy-MM-dd HH:mm:ss"></s:date></td>
						<td><s:date name="endTime" format="yyyy-MM-dd HH:mm:ss"></s:date></td>
						<td><s:property value="max" /></td>
						<td><s:property value="min" /></td>
						<td><s:property value="mean" /></td>
						<td><s:property value="succMean" /></td>
						<td><s:property value="stdDev" /></td>
						<td><s:property value="succStdDev" /></td>
						<td><s:property value="throughput" /></td>
						<td><s:property value="failRate" /></td>
						<td><s:property value="succCount" /></td>
						<td><s:property value="failCount" /></td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</div>
	<!-- /container -->

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="./bootstrap-3.3.2/jquery-1.11.1.min.js"></script>
	<script src="./bootstrap-3.3.2/dist/js/bootstrap.min.js"></script>
	<script src="./bootstrap-3.3.2/assets/js/docs.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script
		src="./bootstrap-3.3.2/assets/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
