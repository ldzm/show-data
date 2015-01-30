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
		
		$.post('optionAction', $("#responseTime").serializeArray(), function(data) {
			option = data["option"];

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
				var myChart = ec.init(document.getElementById('main'));
				// 为echarts对象加载数据 
				myChart.setOption(option);
			});
		});
	}
</script>
<style type="text/css">
label[for="requestType"] {
	color: black;
}

label[for="interval"] {
	color: black;
}

label[for="success"] {
	color: black;
}

label[for="filedir"] {
	color: black;
}

label[for="basedir"] {
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
						<li class="active"><a href="home">请求分析页面</a></li>
						<li><a href="analysis">请求其他分析项页面</a></li>
					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
			<!--/.container-fluid -->
		</nav>

		<!-- Main component for a primary marketing message or call to action -->
		<div class="jumbotron">
			<s:form id="responseTime" method="post">
				<s:select id="requestType" name="requestType" label="请选择请求类型"
					list="requestTypeMap" listKey="key" listValue="value"
					value="HTTP Request">
				</s:select>
				<s:select id="success" name="success" label="成功/失败"
					list="#{'true':'成功', 'false':'失败'}" listKey="key" listValue="value"
					value="true">
				</s:select>
				<s:textfield id="basedir" label="文件所在HDFS" name="basedir"
					value="hdfs://cp01-rdqa-dev362.cp01.baidu.com:9000" />
				<s:textfield id="filedir" label="文件相对HDFS路径" name="filedir"
					value="/art/output/part-00000" />
				<s:textfield id="interval" label="时间间隔" name="interval" value="10" />
				<tr>
					<td><input type="button" value="提交" onClick="getLineOption();" />
					</td>
				</tr>
			</s:form>
			<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
			<div id="main" style="height: 400px"></div>
			<!-- ECharts单文件引入 -->
		</div>
	</div>
	<!-- /container -->


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