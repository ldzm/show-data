<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title><s:text name="HelloWorld.message" /></title>
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
<script type="text/javascript" src="jquery-1.11.2.js"></script>
<script type="text/javascript">
	var option = null;

	var gotClick = function() {
		$.post('jsonExample', $("#form1").serializeArray(), function(data) {
			option = data["option"];
		});

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
	}
</script>
</head>
<body>
	<s:form id="form1" method="post">
		<s:textfield name="field1" label="field1" />
		<s:textfield name="field2" label="field2" />
		<s:textfield name="field3" label="field3" />
		<tr>
			<td colspan="2"><input type="button" value="submit"
				onClick="gotClick();" /></td>
		</tr>
	</s:form>
	<div id="show"></div>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	<div id="main" style="height: 400px"></div>
	<!-- ECharts单文件引入 -->

</body>
</html>
