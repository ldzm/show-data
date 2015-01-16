package example;
import java.io.File;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import service.OptionService;
import service.ParserFileService;
import bean.AverageRequestTimeBean;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * <code>Set welcome message.</code>
 */
public class JSONExample extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JSON option;
	private OptionService optionService;
	private ParserFileService parserFileService;
	
	public JSON getOption() {
		return option;
	}
	public void setOption(JSON option) {
		this.option = option;
	}

	public String execute() {
		optionService = new OptionService();
		parserFileService = new ParserFileService();
		
		String filepath = JSONExample.class.getClassLoader().getResource("option/average_request_time.json").getPath();
		option = optionService.getAverageRequestTimeOptionAsJSON(new File(filepath));
		filepath = JSONExample.class.getClassLoader().getResource("result/part-00000").getPath();
		
		List<String> label = Lists.newArrayList("HTTP Request", "JAVA Request");
		AverageRequestTimeBean bean = parserFileService.getAverageRequestTimeBean(new File(filepath), label);
		
		JSONObject jsonObject = (JSONObject)option;
		
		// set legend
		JSONObject legend = jsonObject.getJSONObject("legend");
		legend.remove("data");
		legend.accumulate("data", bean.getLegend());
		jsonObject.remove("legend");
		jsonObject.accumulate("legend", legend);
		// set xAxis
		JSONArray xAxis = jsonObject.getJSONArray("xAxis");
		JSONObject xAxis0 = xAxis.getJSONObject(0);
		xAxis0.remove("data");
		xAxis0.accumulate("data", bean.getxAxis());
		xAxis.set(0, xAxis0);
		jsonObject.remove("xAxis");
		jsonObject.accumulate("xAxis", xAxis);
		
		// set series
		JSONArray series = jsonObject.getJSONArray("series");
		JSONObject series0 = series.getJSONObject(0);
		series0.remove("data");
		series0.accumulate("data", bean.getSeries().get(0).getData());
		series0.remove("name");
		series0.accumulate("name", bean.getSeries().get(0).getName());
		series.set(0, series0);
		
		JSONObject series1 = series.getJSONObject(1);
		series1.remove("data");
		series1.accumulate("data", bean.getSeries().get(1).getData());
		series1.remove("name");
		series1.accumulate("name", bean.getSeries().get(1).getName());
		series.set(1, series1);
		jsonObject.remove("series");
		jsonObject.accumulate("series", series);
		
		option = jsonObject;
		
		System.out.println(option.toString());
		
		return Action.SUCCESS;
	}
}
