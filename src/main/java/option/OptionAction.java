package option;
import java.io.File;
import java.util.List;

import org.apache.hadoop.fs.Path;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import service.HDFSService;
import service.OptionService;
import service.ParserFileService;
import bean.LineOptionBean;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


public class OptionAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private Integer interval;
	private Boolean success;
	private String requestType;
	private String basedir;
	private String filedir;
	private JSON option;
	private OptionService optionService;
	private ParserFileService parserFileService;	
    private HDFSService hdfsService;
	
    public OptionAction() {
		optionService = new OptionService();
		parserFileService = new ParserFileService();
		hdfsService = new HDFSService();
    }
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getBasedir() {
		return basedir;
	}
	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}
	public String getFiledir() {
		return filedir;
	}
	public void setFiledir(String filedir) {
		this.filedir = filedir;
	}
	public JSON getOption() {
		return option;
	}
	public void setOption(JSON option) {
		this.option = option;
	}

	@Override
	public String execute() {
		
		String filepath = OptionAction.class.getClassLoader().getResource("option/average_request_time.json").getPath();
		option = optionService.getAverageRequestTimeOptionAsJSON(new File(filepath));
	
		hdfsService.setBasePath(basedir);
		List<String> linesContent = null;
		try {
			linesContent = hdfsService.getLineContents(new Path(filedir));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer divisor = parserFileService.getDivisor(linesContent, requestType, success);
		
		System.out.println(divisor);
		LineOptionBean bean = parserFileService.getLineOptionBean(linesContent, requestType, success, interval, divisor);
		
		JSONObject jsonObject = (JSONObject)option;
		
		// set title
		JSONObject title = JSONObject.fromObject(bean.getTitle());
		jsonObject.remove("title");
		jsonObject.accumulate("title", title);
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
		
		return Action.SUCCESS;
	}
}
